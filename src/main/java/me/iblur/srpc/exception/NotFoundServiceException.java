package me.iblur.srpc.exception;

/**
 * 没有RPC服务异常
 */
public class NotFoundServiceException extends RpcException {

    private static final long serialVersionUID = 4925734531028094643L;

    public NotFoundServiceException(String message) {
        super(message);
    }

    public NotFoundServiceException(String message, Throwable t){
        super(message, t);
    }

}