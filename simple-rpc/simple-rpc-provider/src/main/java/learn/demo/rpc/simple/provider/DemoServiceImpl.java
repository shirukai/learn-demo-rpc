package learn.demo.rpc.simple.provider;

import learn.demo.rpc.simple.api.DemoService;

/**
 * Created by shirukai on 2019-06-21 10:55
 * 接口实现类
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "This is simple RPC service.\nHello " + name;
    }

    @Override
    public String sayGoodbye(String name) {
        return "This is simple RPC service.\nGoodbye " + name;
    }
}
