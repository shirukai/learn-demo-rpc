package learn.demo.rpc.zk.core.registry;

import com.alibaba.fastjson.JSON;

/**
 * Created by shirukai on 2019-06-25 16:34
 * Provider信息
 */
public class ProviderInfo {
    /**
     * 提供者ID
     */
    private String id;
    /**
     * 提供者地址
     */
    private String address;
    /**
     * 提供者端口
     */
    private int port;

    public String getId() {
        return id;
    }

    public ProviderInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ProviderInfo setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ProviderInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return "ProviderInfo{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
