package learn.demo.rpc.zk.core.nio;

import java.io.*;

/**
 * Created by shirukai on 2018/8/21
 */
public class ObjectUtils {
    private ObjectUtils() {
    }

    public static byte[] toBytes(Object message) {
        byte[] bytes = null;
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArray)) {
            outputStream.writeObject(message);
            outputStream.flush();
            bytes = byteArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static <T> T toObject(byte[] bytes) {
        Object message = null;
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(in)) {
            message = inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) message;
    }
}
