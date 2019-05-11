package me.iblur.srpc;

import me.iblur.srpc.exception.RpcException;
import me.iblur.srpc.exchange.RpcRequest;
import me.iblur.srpc.exchange.RpcResponse;

/**
 * RPC调用者？？？
 */
public class RpcInvoker<T> {

    /**
     * RPC客户端
     */
    private RpcClient rpcClient;

    /**
     * 接口服务类
     */
    private Class<T> interfaceClass;

    /**
     * 构造一个RpcInvoker
     *
     * @param host           RPC服务器端监听host
     * @param port           RPC服务器端监听的port
     * @param interfaceClass 目标服务接口类
     */
    public RpcInvoker(String host, int port, Class<T> interfaceClass) {
        this.rpcClient = new RpcClient(host, port);
        this.interfaceClass = interfaceClass;
    }

    /**
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @param args           方法参数
     * @return 结果
     * @throws Throwable 服务端及客户端异常，包括RPC服务抛出的异常
     */
    public Object invoke(String methodName, Class<?>[] parameterTypes, Object[] args)
            throws Throwable {
        RpcResponse rpcResponse = null;
        try {
            // 实例化一个RpcRequest
            RpcRequest rpcRequest = new RpcRequest().newRpcRequest(this.interfaceClass.getName(), methodName,
                    parameterTypes, args);
            // 通过RPC Client将数据发送到RPC Server
            this.rpcClient.writeData(rpcRequest);
            // 从RPC Server读取响应结果
            rpcResponse = (RpcResponse) this.rpcClient.readData();
        } finally {
            this.rpcClient.close();
        }
        // 异常处理
        if (null == rpcResponse) {
            throw new RpcException("RPC调用为获取到结果");
        }
        if (null != rpcResponse.getThrowable()) {
            throw rpcResponse.getThrowable();
        }
        // 返回真实结果给服务调用方
        return rpcResponse.getResponse();
    }

}