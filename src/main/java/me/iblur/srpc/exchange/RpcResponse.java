package me.iblur.srpc.exchange;

import java.io.Serializable;

public class RpcResponse implements Serializable {

    private static final long serialVersionUID = -5573289211420999714L;

    private Object response;

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