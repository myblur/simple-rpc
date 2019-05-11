package me.iblur.api;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    public void noResponse(String name) {
        System.out.println("Hello, " + name);
    }

}