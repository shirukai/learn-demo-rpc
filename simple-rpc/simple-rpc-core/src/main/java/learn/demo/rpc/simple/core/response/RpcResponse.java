package learn.demo.rpc.simple.core.response;

import java.io.Serializable;

/**
 * Created by shirukai on 2019-06-21 14:43
 * RPC response
 */
public class RpcResponse implements Serializable {
    public static String SUCCEED = "succeed";
    public static String FAILED = "failed";
    private static final long serialVersionUID = 6595683424889346485L;

    /**
     * 响应状态
     */
    private String status = "succeed";
    /**
     * 响应信息，如异常信息
     */
    private String message;

    /**
     * 响应数据，返回值
     */
    private Object data;


    public String getStatus() {
        return status;
    }

    public RpcResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RpcResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RpcResponse setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
