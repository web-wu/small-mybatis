package com.tabwu.mybatis.session.defaults;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.SqlSession;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 16:17
 * @DESCRIPTION:
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了：=>" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        // statement == namespace + id 即方法的全限定名
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        System.out.println(("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql()));
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type,this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
