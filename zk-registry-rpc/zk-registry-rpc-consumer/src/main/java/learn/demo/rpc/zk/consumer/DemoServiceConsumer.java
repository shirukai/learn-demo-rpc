package learn.demo.rpc.zk.consumer;

import learn.demo.rpc.zk.core.client.RpcConsumer;
import learn.demo.rpc.zk.api.DemoService;

/**
 * Created by shirukai on 2019-06-25 18:04
 * 消费者
 */
public class DemoServiceConsumer {
    public static void main(String[] args) {
        RpcConsumer consumer = new RpcConsumer();
        consumer.setZKConnectString("localhost:2181");
        consumer.setInterface(DemoService.class);

        DemoService service = consumer.get();

        System.out.println(service.sayGoodbye("hahah"));
    }
}
