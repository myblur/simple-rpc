package me.iblur.srpc;

import me.iblur.srpc.exception.NotFoundServiceException;
import me.iblur.srpc.exception.RpcException;
import me.iblur.srpc.exchange.RpcRequest;
import me.iblur.srpc.exchange.RpcResponse;
import me.iblur.srpc.utils.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcServer {

    /**
     * 服务端绑定的IP
     */
    private String host;

    /**
     * 服务端绑定的端口
     */
    private int port;

    private ServerSocket server;

    /**
     * RPC服务映射，不支持一个服务接口有多个服务实现
     * 服务接口类名 => 服务接口实现类
     */
    private ConcurrentMap<String, Object> serviceMapping = new ConcurrentHashMap<>();

    /**
     * RpcServer的运行状态
     */
    private volatile boolean running = false;

    public RpcServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 启动服务端，并监听客户端Socket的连接请求
     *
     * @throws IOException
     */
    public void startServer() throws IOException {
        // 注册一个ShutdownHook，用于在应用关闭时来调用RpcServer的关闭逻辑
        Runtime.getRuntime().addShutdownHook(new Thread(RpcServer.this::stop));
        server = new ServerSocket();
        server.bind(new InetSocketAddress(this.host, this.port));
        System.out.println("服务器启动成功，绑定地址：" + this.server.getLocalSocketAddress());
        running = true;
        while (running) {
            try {
                Socket socket = this.server.accept();
                System.out.println("接收到客户端连接：" + socket);
                new Thread(new RequestHandler(socket),
                        "RequestHandler[" + socket.getRemoteSocketAddress().toString() + "] Thread").start();
            } catch (IOException e) {
                System.out.println("接收连接异常：" + e);
            }
        }
    }

    /**
     * 关闭RpcServer
     */
    public void stop() {
        System.out.println("服务器关闭");
        running = false;
        try {
            this.server.close();
        } catch (Exception e) {
            System.out.println("服务器关闭异常：" + e);
        }
    }

    /**
     * 注册一个接口服务
     * RPC服务接口 => RPC接口服务实例
     *
     * @param interfaceClass RPC接口服务类
     * @param serviceObject  RPC接口服务实例
     */
    public void addServiceMapping(Class<?> interfaceClass, Object serviceObject) {
        if (running) {
            throw new IllegalStateException("服务已启动，不能再次注册服务");
        }
        if (null != serviceMapping.get(interfaceClass.getName())) {
            throw new RpcException("接口[" + interfaceClass.getName() + "]已经存在一个实现");
        }
        System.out.println("注册RPC服务[" + interfaceClass.getName() + " => " + serviceObject + "]");
        this.serviceMapping.put(interfaceClass.getName(), serviceObject);
    }

    /**
     * 客户端请求处理
     */
    private final class RequestHandler implements Runnable {

        private Socket socket;

        private ObjectInputStream in;

        private ObjectOutputStream out;

        RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            RpcResponse rpcResponse = new RpcResponse();
            try {
                // 读取数据
                RpcRequest rpcRequest = readData();
                // 调用服务
                Object result = invokeService(rpcRequest);
                // 写出数据
                rpcResponse.setResponse(result);
                writeData(rpcResponse);
            } catch (IOException e) {
                System.out.println("从Socket中读取或写入数据异常：" + e);
            } catch (Exception e) {
                rpcResponse.setThrowable(e);
                try {
                    writeData(rpcResponse);
                } catch (IOException ie) {
                    System.out.println("服务端响应数据异常：" + ie);
                }
            } finally {
                IOUtils.close(this.socket, this.in, this.out);
            }
        }

        /**
         * 将响应结果写出
         *
         * @param response 响应结果
         * @throws IOException 写出结果发生异常
         */
        private void writeData(RpcResponse response) throws IOException {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            out.writeObject(response);
            out.flush();
        }

        /**
         * 从Socket中读取数据
         *
         * @return Rpc请求
         * @throws IOException            读取数据异常
         * @throws ClassNotFoundException Rpc请求，或者Rpc请求中所带的class在服务端不存在
         */
        private RpcRequest readData() throws IOException, ClassNotFoundException {
            this.in = new ObjectInputStream(this.socket.getInputStream());
            return (RpcRequest) in.readObject();
        }

        /**
         * 调用服务的方法
         *
         * @param rpcRequest Rpc请求
         * @return 调用结果
         */
        private Object invokeService(RpcRequest rpcRequest) {
            Object serviceObject = serviceMapping.get(rpcRequest.getInterfaceClassName());
            if (null == serviceObject) {
                throw new NotFoundServiceException("服务类[" + rpcRequest.getInterfaceClassName() + "]没有找到可用的服务");
            }
            try {
                Method method = serviceObject.getClass().getMethod(rpcRequest.getMethodName(),
                        rpcRequest.getParameterTypes());
                return method.invoke(serviceObject, rpcRequest.getArguments());
            } catch (NoSuchMethodException | SecurityException e) {
                throw new NotFoundServiceException("服务类[" + rpcRequest.getInterfaceClassName() + "]没有找到目标方法", e);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new NotFoundServiceException("服务类[" + rpcRequest.getInterfaceClassName() + "]调用目标方法异常", e);
            }
        }
    }

}