package me.iblur.srpc.exchange;

import java.io.Serializable;

/**
 * Rpc请求参数
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -9213158787762981233L;

    /**
     * 目标接口类
     */
    private String interfaceClassName;

    /**
     * 目标接口方法名
     */
    private String methodName;

    /**
     * 目标接口方法参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 目标接口方法参数
     */
    private Object[] arguments;


    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the parameterTypes
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    /**
     * @param parameterTypes the parameterTypes to set
     */
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * @return the arguments
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public RpcRequest newRpcRequest(String interfaceClassName, String methodName, Class<?>[] parameterTypes,
            Object[] args) {
        this.setInterfaceClassName(interfaceClassName);
        this.setMethodName(methodName);
        this.setParameterTypes(parameterTypes);
        this.setArguments(args);
        return this;
    }
}