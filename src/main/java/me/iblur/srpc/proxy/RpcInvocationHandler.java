package me.iblur.srpc.proxy;

import me.iblur.srpc.RpcInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用JDK动态代理来无感知的完成RPC调用的内部细节
 */
public class RpcInvocationHandler implements InvocationHandler {

    private RpcInvoker<?> invoker;

    public RpcInvocationHandler(RpcInvoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 排除掉所有定义在Object.class的方法，已经toString、hashCode、equals方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }
        return invoker.invoke(methodName, parameterTypes, args);
    }

}