package com.tabwu.mybatis.plugin;

import java.util.Properties;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/31 16:30
 * @DESCRIPTION: 插件拦截接口
 */
public interface Interceptor {

    //拦截， 使用方实现
    Object intercept(Invocation invocation) throws Throwable;

    // 代理
    default Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    // 设置属性
    default void setProperties(Properties properties) {
        // NOP
    }
}
