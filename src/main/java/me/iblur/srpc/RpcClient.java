package me.iblur.srpc;

import me.iblur.srpc.exception.RpcNetworkException;
import me.iblur.srpc.utils.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * RPC客户端
 *
 * @author qinx
 * @date 2019/5/10 15:26
 */
public class RpcClient {

    private Socket socket;

    private ObjectOutputStream out;

    private ObjectInputStream in;

    public RpcClient(String host, int port) {
        this.socket = new Socket();
        try {
            this.socket.connect(new InetSocketAddress(host, port), 3000);
        } catch (Exception e) {
            throw new RpcNetworkException("连接服务器[" + host + ":" + port + "]异常", e);
        }

    }

    /**
     * 写数据
     *
     * @param object 数据
     * @throws IOException 写出异常
     */
    public void writeData(Object object) throws IOException {
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.out.writeObject(object);
        this.out.flush();
    }

    /**
     * 读数据
     *
     * @return 读去结果
     * @throws IOException            读取异常
     * @throws ClassNotFoundException 返回的结果的class在客户端不存在
     */
    public Object readData() throws IOException, ClassNotFoundException {
        this.in = new ObjectInputStream(this.socket.getInputStream());
        return this.in.readObject();
    }

    public void close() {
        IOUtils.close(this.in, this.out, this.socket);
    }

}
