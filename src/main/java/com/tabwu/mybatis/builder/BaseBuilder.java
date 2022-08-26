package com.tabwu.mybatis.builder;

import com.tabwu.mybatis.session.Configuration;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:25
 * @DESCRIPTION:
 */
public class BaseBuilder {

    protected Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
