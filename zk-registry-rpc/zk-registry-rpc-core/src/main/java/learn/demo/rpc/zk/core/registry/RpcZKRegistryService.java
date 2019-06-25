package learn.demo.rpc.zk.core.registry;

import com.alibaba.fastjson.JSON;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by shirukai on 2019-06-25 16:42
 * Rpc注册服务
 */
public class RpcZKRegistryService {
    private static final Logger log = LoggerFactory.getLogger(RpcZKRegistryService.class);
    private static final String NAMESPACE = "zk-rpc";
    private static final String RPC_PROVIDER_NODE = "/provider";


    private final Map<String, ProviderInfo> remoteProviders = new HashMap<>();
    private CuratorFramework zkClient;

    public RpcZKRegistryService(String zkConnectString) {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        // 获取客户端
        this.zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkConnectString)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace(NAMESPACE)
                .build();
        this.zkClient.start();
    }

    /**
     * 注册服务
     *
     * @param providerInfo 提供者信息
     */
    public void register(ProviderInfo providerInfo) {
        String nodePath = RPC_PROVIDER_NODE + "/" + providerInfo.getId();
        try {
            // 判断节点存不存在，不存在则创建，存在则报异常
            Stat stat = zkClient.checkExists().forPath(nodePath);
            if (stat == null) {
                // 创建临时节点
                zkClient.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(nodePath, providerInfo.toJSONString().getBytes());
            } else {
                log.error("The provider already exists.{}", providerInfo.toJSONString());
            }
        } catch (Exception e) {
            log.error("Registration provider failed.{}", e.getMessage());
        }
    }


    /**
     * 订阅服务
     *
     * @param id 提供者ID,接口名字
     */
    public void subscribe(String id) {
        try {
            // 获取所有的Provider
            List<String> providerIds = zkClient.getChildren().forPath(RPC_PROVIDER_NODE);
            for (String providerId : providerIds) {
                // 如果与订阅服务相同，则获取节点信息
                if (providerId.contains(id)) {
                    String nodePath = RPC_PROVIDER_NODE + "/" + providerId;
                    byte[] data = zkClient.getData().forPath(nodePath);
                    ProviderInfo info = JSON.parseObject(data, ProviderInfo.class);
                    this.remoteProviders.put(providerId, info);
                }
            }

            // 添加监听事件
            addProviderWatch(id);
        } catch (Exception e) {
            log.error("Subscription provider failed.");
        }

    }

    /**
     * 添加监听事件
     *
     * @param id 提供者ID，接口名字
     */
    private void addProviderWatch(String id) throws Exception {
        // 创建子节点缓存
        final PathChildrenCache childrenCache = new PathChildrenCache(this.zkClient, RPC_PROVIDER_NODE, true);
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        // 添加子节点监听事件
        childrenCache.getListenable().addListener((client, event) -> {
            String nodePath = event.getData().getPath();
            // 如果监听节点为订阅的ProviderID
            if (nodePath.contains(id)) {

                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    // 节点移除
                    this.remoteProviders.remove(nodePath);

                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    byte[] data = event.getData().getData();
                    ProviderInfo info = JSON.parseObject(data, ProviderInfo.class);
                    // 节点添加
                    this.remoteProviders.put(nodePath, info);
                }
            }
        });
    }

    public Map<String, ProviderInfo> getRemoteProviders() {
        return remoteProviders;
    }
}
