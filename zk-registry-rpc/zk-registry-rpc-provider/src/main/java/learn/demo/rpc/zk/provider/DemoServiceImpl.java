package learn.demo.rpc.zk.provider;


import learn.demo.rpc.zk.api.DemoService;

/**
 * Created by shirukai on 2019-06-21 10:55
 * 接口实现类
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "This is RPC service base in zookeeper registry.\nHello " + name;
    }

    @Override
    public String sayGoodbye(String name) {
        return "This is  RPC service base in zookeeper registry.\nGoodbye " + name;
    }
}
