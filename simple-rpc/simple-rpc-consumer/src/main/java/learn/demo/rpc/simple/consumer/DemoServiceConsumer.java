package learn.demo.rpc.simple.consumer;

import learn.demo.rpc.simple.api.DemoService;
import learn.demo.rpc.simple.core.client.RpcConsumer;

/**
 * Created by shirukai on 2019-06-21 11:29
 * 消费者
 */
public class DemoServiceConsumer {
    public static void main(String[] args) {
        RpcConsumer consumer = new RpcConsumer();
        consumer.setAddress("127.0.0.1");
        consumer.setPort(9090);
        consumer.setInterface(DemoService.class);

        DemoService service = consumer.get();

        System.out.println(service.sayGoodbye("hahah"));
    }
}
