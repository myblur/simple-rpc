package me.iblur.srpc.utils;

import java.io.Closeable;

/**
 * @author qinx
 * @date 2019/5/10 15:32
 */
public final class IOUtils {

    public static void close(Closeable... closeables) {
        if (null != closeables && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                if (null != closeable) {
                    try {
                        closeable.close();
                    } catch (Exception e) {
                        System.out.println("关闭资源异常：" + e);
                    }
                }
            }
        }
    }

}
