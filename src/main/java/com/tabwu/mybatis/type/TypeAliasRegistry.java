package com.tabwu.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 15:52
 * @Description: 类型别名注册器
 */
public class TypeAliasRegistry {

    private final Map<String,Class<?>> TYPE_ALIAS = new HashMap<>();

    public TypeAliasRegistry() {
        // 基本数据类型 别名注册
        registryTypeAlias("string", String.class);
        registryTypeAlias("byte", Byte.class);
        registryTypeAlias("short", Short.class);
        registryTypeAlias("int", Integer.class);
        registryTypeAlias("integer", Integer.class);
        registryTypeAlias("long", Long.class);
        registryTypeAlias("float", Float.class);
        registryTypeAlias("double", Double.class);
        registryTypeAlias("boolean", Boolean.class);
    }

    public void registryTypeAlias(String alias, Class<?> type) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIAS.put(key,type);
    }

    public <T> Class<T> getAliasType(String alias) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        return (Class<T>) TYPE_ALIAS.get(key);
    }
}
