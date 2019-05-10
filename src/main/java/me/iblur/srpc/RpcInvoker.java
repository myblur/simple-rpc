package me.iblur.srpc;

import me.iblur.srpc.exception.RpcException;
import me.iblur.srpc.exchange.RpcRequest;
import me.iblur.srpc.exchange.RpcResponse;

public class RpcInvoker<T> {

    private RpcClient rpcClient;
    private Class<T> interfaceClass;

    public RpcInvoker(String host, int port, Class<T> interfaceClass) {
        this.rpcClient = new RpcClient(host, port);
        this.interfaceClass = interfaceClass;
    }

    public Object invoke(String methodName, Class<?>[] parameterTypes, Object[] args)
            throws Throwable {
        RpcResponse rpcResponse = null;
        try {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceClassName(this.interfaceClass.getName());
            rpcRequest.setMethodName(methodName);
            rpcRequest.setParameterTypes(parameterTypes);
            rpcRequest.setArguments(args);
            this.rpcClient.writeData(rpcRequest);
            rpcResponse = (RpcResponse) this.rpcClient.readData();
        } finally {
            this.rpcClient.close();
        }
        if (null == rpcResponse) {
            throw new RpcException("RPC调用为获取到结果");
        }
        if (null != rpcResponse.getThrowable()) {
            throw rpcResponse.getThrowable();
        }
        return rpcResponse.getResponse();
    }

}