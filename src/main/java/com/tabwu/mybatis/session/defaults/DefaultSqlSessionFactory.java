package com.tabwu.mybatis.session.defaults;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.SqlSession;
import com.tabwu.mybatis.session.SqlSessionFactory;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 17:04
 * @DESCRIPTION:
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
