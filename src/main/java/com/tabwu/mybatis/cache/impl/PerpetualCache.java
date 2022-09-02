package com.tabwu.mybatis.cache.impl;

import com.alibaba.fastjson.JSON;
import com.tabwu.mybatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/9/2 10:49
 * @DESCRIPTION: 一级缓存，在 Session 生命周期内一直保持，每创建新的 OpenSession 都会创建一个缓存器 PerpetualCache
 */
public class PerpetualCache implements Cache {

    Logger logger = LoggerFactory.getLogger(PerpetualCache.class);

    private String id;

    // 使用HashMap存放一级缓存数据，session 生命周期较短，正常情况下数据不会一直在缓存存放
    private Map<Object, Object> cache = new HashMap<>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        Object value = cache.get(key);
        if (value != null) {
            logger.info("触发一级缓存key ==> {}, value ==>, {}", key ,JSON.toJSONString(value));
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }
}
