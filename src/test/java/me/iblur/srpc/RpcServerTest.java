package me.iblur.srpc;

import me.iblur.api.HelloService;
import me.iblur.api.HelloServiceImpl;
import org.testng.annotations.Test;

import java.io.IOException;

public class RpcServerTest {

    @Test
    public void testStartServer() throws IOException {
        RpcServer rpcServer = new RpcServer("localhost", 10210);
        rpcServer.addServiceMapping(HelloService.class, new HelloServiceImpl());
        rpcServer.startServer();
    }

}