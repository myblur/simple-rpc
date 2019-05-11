package me.iblur.srpc.exchange;

import java.io.Serializable;

/**
 * RPC响应，网络传输需要实现Serializable接口
 */
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = -5573289211420999714L;

    /**
     * 响应结果
     */
    private Object response;

    /**
     * 服务端的异常，包括服务方法抛出的异常
     */
    private Throwable throwable;

    /**
     * @return the response
     */
    public Object getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * @param throwable the throwable to set
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}