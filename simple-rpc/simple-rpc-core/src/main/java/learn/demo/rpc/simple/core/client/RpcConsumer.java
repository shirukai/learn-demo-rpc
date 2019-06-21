package learn.demo.rpc.simple.core.client;

import learn.demo.rpc.simple.core.client.proxy.RpcInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * Created by shirukai on 2019-06-21 16:11
 * 生产者构建器
 */
public class RpcConsumer {
    private String address;
    private int port;

    private Class<?> interfaceClass;

    public RpcConsumer setAddress(String address) {
        this.address = address;
        return this;
    }

    public RpcConsumer setPort(int port) {
        this.port = port;
        return this;
    }

    public RpcConsumer setInterface(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return this;
    }

    public <T> T get() {
        RpcClient client = new RpcClient();
        client.setAddress(address);
        client.setPort(port);
        // 实例化RPC代理处理器
        RpcInvocationHandler handler = new RpcInvocationHandler(client);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, handler);
    }
}
