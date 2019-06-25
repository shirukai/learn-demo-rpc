package learn.demo.rpc.zk.core.client;


import learn.demo.rpc.zk.core.response.RpcResponse;
import learn.demo.rpc.zk.core.request.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by shirukai on 2019-06-21 15:42
 * Rpc客户端
 */
public class RpcClient {
    /**
     * 服务地址
     */
    private String address;

    /**
     * 服务端口
     */
    private int port;

    public RpcResponse send(RpcRequest rpcRequest) throws Exception {

        Socket socket = new Socket(address, port);

        //请求序列化
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        //将请求发给服务提供方
        objectOutputStream.writeObject(rpcRequest);

        // 将响应体反序列化
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        Object response = objectInputStream.readObject();
        if (response instanceof RpcResponse) {
            return (RpcResponse) response;
        }
        throw new RuntimeException("Return error");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
