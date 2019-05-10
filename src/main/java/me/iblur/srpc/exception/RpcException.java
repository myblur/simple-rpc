package me.iblur.srpc.exception;

public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -4515534178939511614L;

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable t){
        super(message, t);
    }

}