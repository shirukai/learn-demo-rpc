package learn.demo.rpc.zk.provider;

import learn.demo.rpc.zk.core.server.RpcProvider;
import learn.demo.rpc.zk.api.DemoService;

/**
 * Created by shirukai on 2019-06-25 17:56
 * 提供者
 */
public class DemoServiceProvider {
    public static void main(String[] args) {
        DemoServiceImpl demoService = new DemoServiceImpl();

        RpcProvider<DemoService> provider = new RpcProvider<>();

        provider.setInterfaceClass(DemoService.class)
                .setZKConnectString("localhost:2181")
                .setRef(demoService);

        provider.export(9090);

    }
}
