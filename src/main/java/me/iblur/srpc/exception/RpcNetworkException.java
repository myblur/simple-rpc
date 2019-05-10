package me.iblur.srpc.exception;

public class RpcNetworkException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RpcNetworkException(String message) {
        super(message);
    }

    public RpcNetworkException(String message, Throwable t) {
        super(message, t);
    }

}