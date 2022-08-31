package com.tabwu.mybatis.plugin;

import java.lang.reflect.Method;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/31 16:23
 * @DESCRIPTION: 方法签名
 */
public @interface Signature {

    //被拦截类
    Class<?> type();

    //被拦截类的方法
    String method();

    //被拦截类的方法参数
    Class<?>[] args();
}
