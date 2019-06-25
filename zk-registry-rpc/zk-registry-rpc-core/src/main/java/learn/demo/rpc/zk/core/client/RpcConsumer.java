package learn.demo.rpc.zk.core.client;


import learn.demo.rpc.zk.core.client.proxy.RpcInvocationHandler;
import learn.demo.rpc.zk.core.registry.ProviderInfo;
import learn.demo.rpc.zk.core.registry.RpcZKRegistryService;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by shirukai on 2019-06-21 16:11
 * 生产者构建器
 */
public class RpcConsumer {
    private String azConnectString;
    private Class<?> interfaceClass;
    private RpcZKRegistryService registryService;


    public RpcConsumer setZKConnectString(String zkConnectString) {
        this.registryService = new RpcZKRegistryService(zkConnectString);
        return this;
    }


    public RpcConsumer setInterface(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return this;
    }


    public <T> T get() {
        List<ProviderInfo> providers = lookupProviders();

        ProviderInfo provider = chooseTarget(providers);

        RpcClient client = new RpcClient();
        client.setAddress(provider.getAddress());
        client.setPort(provider.getPort());
        // 实例化RPC代理处理器
        RpcInvocationHandler handler = new RpcInvocationHandler(client);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, handler);
    }

    /**
     * 获取所有Providers
     *
     * @return list
     */
    private List<ProviderInfo> lookupProviders() {
        // 订阅服务
        registryService.subscribe(interfaceClass.getName());
        // 获取所有Provider
        Map<String, ProviderInfo> providers = registryService.getRemoteProviders();
        return new ArrayList<>(providers.values());
    }

    /**
     * 模拟负载均衡
     *
     * @param providers provider 列表
     * @return ProviderInfo
     */
    private static ProviderInfo chooseTarget(List<ProviderInfo> providers) {
        if (providers == null || providers.isEmpty()) {
            throw new RuntimeException("providers has not exits!");
        }
        return providers.get(0);
    }
}
