package learn.demo.rpc.simple.provider;


import learn.demo.rpc.simple.api.DemoService;
import learn.demo.rpc.simple.core.server.RpcProvider;

/**
 * Created by shirukai on 2019-06-21 10:56
 * 服务提供者
 */
public class DemoServiceProvider {

    public static void main(String[] args) {
        DemoServiceImpl demoService = new DemoServiceImpl();

        RpcProvider<DemoService> provider = new RpcProvider<>();
        provider.setInterfaceClass(DemoService.class)
                .setRef(demoService);

        provider.export(9090);
    }


}
