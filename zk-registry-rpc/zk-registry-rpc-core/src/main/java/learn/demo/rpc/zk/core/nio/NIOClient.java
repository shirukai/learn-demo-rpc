package learn.demo.rpc.zk.core.nio;

import learn.demo.rpc.zk.core.request.RpcRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by shirukai on 2019-06-25 19:09
 */
public class NIOClient {
    public static void main(String[] args) {
        try {
            // 1. 创建一个信道,并绑定远程服务器的ip和端口
            SocketChannel channel = SocketChannel.open();
            // 设置服务器的address
            InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 9001);
            // 2. 连接远程服务器
            channel.connect(remote);
            channel.configureBlocking(false);
            // 3. 创建一个Socket选择器
            Selector selector = Selector.open();
            // 4. 将选择器注册到信道中
            channel.register(selector, SelectionKey.OP_READ);
            // 5. 启动一个线程用来接收服务器消息
            new Thread(() -> {
                while (true) {
                    //读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    try {
                        length = channel.read(buffer);
                        if (length > 0) {
                            System.out.println(new String(buffer.array(), 0, length));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            RpcRequest request = new RpcRequest();
            request.setId("1");
            request.setMethodName("name");
            channel.write(ByteBuffer.wrap(ObjectUtils.toBytes(request)));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
