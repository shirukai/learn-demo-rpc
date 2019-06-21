package learn.demo.rpc.simple.core.server;

import learn.demo.rpc.simple.core.request.RpcRequest;
import learn.demo.rpc.simple.core.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by shirukai on 2019-06-21 16:26
 * RPC provider
 */
public class RpcProvider<T> {
    private static final Logger log = LoggerFactory.getLogger(RpcProvider.class);
    private T ref;

    private Class<?> interfaceClass;

    public void setRef(T ref) {
        this.ref = ref;
    }

    public RpcProvider<T> setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return this;
    }

    public void export(int port) {
        try {
            log.info("The RPC Server is starting, address:{}, bind:{}", InetAddress.getLocalHost().getHostAddress(), port);
            ServerSocket listener = new ServerSocket(port);
            while (true) {
                Socket socket = listener.accept();
                // 接收数据并进行反序列化
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                // 获取请求对象
                Object object = objectInputStream.readObject();

                if (object instanceof RpcRequest) {
                    RpcRequest request = (RpcRequest) object;
                    log.info("Received request:{}", request);
                    // 处理请求
                    RpcResponse response = handleRequest(request);
                    // 将结果返回给客户端
                    log.info("Send response to client.{}", response);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(response);
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RpcResponse handleRequest(RpcRequest request) {
        RpcResponse response = new RpcResponse();
        try {
            log.info("The server is handling request.");
            Method method = interfaceClass.getMethod(request.getMethodName(), request.getParameterTypes());
            Object data = method.invoke(ref, request.getParameters());
            response.setData(data);
        } catch (Exception e) {
            response.setStatus(RpcResponse.FAILED).setMessage(e.getMessage());
        }
        return response;
    }
}
