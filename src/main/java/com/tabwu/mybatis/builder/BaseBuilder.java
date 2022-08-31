package com.tabwu.mybatis.builder;

import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.type.TypeAliasRegistry;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:25
 * @DESCRIPTION:
 */
public class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.getAliasType(alias);
    }

    // 根据别名解析 Class 类型别名注册/事务管理器别名
    protected Class<?> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving class. Cause: " + e, e);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
