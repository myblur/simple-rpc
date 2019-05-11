package me.iblur.srpc;

import me.iblur.api.HelloService;
import me.iblur.srpc.exception.NotFoundServiceException;
import me.iblur.srpc.exception.RpcNetworkException;
import me.iblur.srpc.proxy.RpcInvocationHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;

public class RpcClientTest {

    private HelloService helloService;

    @BeforeMethod
    public void setUp() {
        RpcInvoker<HelloService> invoker = new RpcInvoker<>("localhost", 10210, HelloService.class);
        helloService = (HelloService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{HelloService.class}, new RpcInvocationHandler(invoker));
    }

    @Test(expectedExceptions = RpcNetworkException.class)
    public void testUnConnectServer() {
        helloService.sayHello("Simple RPC");
    }

    @Test(expectedExceptions = NotFoundServiceException.class)
    public void testNoService() {
        helloService.sayHello("Simple RPC");
    }

    @Test
    public void testNormalInvokeService() {
        String result = helloService.sayHello("Simple RPC");
        Assert.assertEquals(result, "Hello, Simple RPC");
    }

    @Test
    public void testNoResponse() {
        helloService.noResponse("Simple RPC");
    }

}