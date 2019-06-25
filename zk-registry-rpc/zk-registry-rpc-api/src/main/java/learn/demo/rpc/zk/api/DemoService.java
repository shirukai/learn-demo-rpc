package learn.demo.rpc.zk.api;

/**
 * Created by shirukai on 2019-06-25 17:55
 * DemoService接口
 */
public interface DemoService {
    String sayHello(String name);

    String sayGoodbye(String name);
}
