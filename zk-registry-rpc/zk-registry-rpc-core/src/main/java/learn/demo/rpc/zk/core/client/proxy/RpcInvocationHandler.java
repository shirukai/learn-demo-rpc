package learn.demo.rpc.zk.core.client.proxy;


import learn.demo.rpc.zk.core.client.RpcClient;
import learn.demo.rpc.zk.core.request.RpcRequest;
import learn.demo.rpc.zk.core.response.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by shirukai on 2019-06-21 15:43
 * RPC 代理处理器
 */
public class RpcInvocationHandler implements InvocationHandler {
    private RpcClient client;

    public RpcInvocationHandler(RpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 构建请求对象
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setMethodName(method.getName())
                .setParameterTypes(method.getParameterTypes())
                .setParameters(args);
        // 使用客户端发送请求
        RpcResponse response = client.send(rpcRequest);

        // 响应成功返回结果
        if (RpcResponse.SUCCEED.equals(response.getStatus())) {
            return response.getData();
        }
        throw new RuntimeException(response.getMessage());
    }
}
