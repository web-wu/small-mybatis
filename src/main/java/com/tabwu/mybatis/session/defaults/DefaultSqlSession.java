package com.tabwu.mybatis.session.defaults;

import com.tabwu.mybatis.executor.Executor;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.RowBounds;
import com.tabwu.mybatis.session.SqlSession;

import java.sql.SQLException;
import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 16:17
 * @DESCRIPTION:
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public int insert(String statement, Object parameter) {
        return this.update(statement,parameter);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return this.update(statement,parameter);
    }

    @Override
    public int update(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.update(ms,parameter, RowBounds.DEFAULT,null,ms.getBoundSql());
        } catch (SQLException e) {
            throw new RuntimeException("Error updating database.  Cause: " + e);
        }
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = this.<T>selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1){
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        try {
            // statement == namespace + id 即方法的全限定名
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            return executor.query(mappedStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLE, mappedStatement.getBoundSql());
        } catch (Exception e) {
            throw new RuntimeException("Error updating database.  Cause: " + e);
        }
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
