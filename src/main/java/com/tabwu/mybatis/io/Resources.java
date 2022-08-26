package com.tabwu.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:00
 * @DESCRIPTION: 配置文件加载器
 */
public class Resources {

    public static Reader getResourceAsReader(String resource) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    /**
     * 直接通过应用类加载器加载classpath路径下的配置文件返回inputStream
     * @param resource
     * @return
     * @throws IOException
     */
    private static InputStream getResourceAsStream(String resource) throws IOException {
        InputStream inputStream = Resources.class.getClassLoader().getResourceAsStream(resource);
        if (inputStream == null) {
            throw new IOException("Could not find resource" + resource);
        }
        return inputStream;
    }

    public static Class<?> classForName(String namespace) throws ClassNotFoundException {
        return Class.forName(namespace);
    }
}
