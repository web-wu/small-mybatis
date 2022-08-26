package com.tabwu.mybatis.session;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.mapping.MappedStatement;

import java.util.HashMap;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:20
 * @DESCRIPTION: 配置文件类
 */
public class Configuration {

    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    protected final HashMap<String, MappedStatement> statementHashMap = new HashMap<>();

    public void addMappers(String packgeName) {
        mapperRegistry.addMappers(packgeName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type,SqlSession sqlSession) {
        return mapperRegistry.getMapper(type,sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        statementHashMap.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return statementHashMap.get(id);
    }

}
