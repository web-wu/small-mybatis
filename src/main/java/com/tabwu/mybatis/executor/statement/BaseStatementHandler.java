package com.tabwu.mybatis.executor.statement;

import com.tabwu.mybatis.executor.Executor;
import com.tabwu.mybatis.executor.resultSet.ResultSetHandler;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/29 15:55
 * @DESCRIPTION:  语句处理器抽象基类
 */
public abstract class BaseStatementHandler implements StatementHandler{

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement ms;

    protected final Object paramterObject;
    protected final ResultSetHandler resultSetHandler;
    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement ms, Object paramterObject, BoundSql boundSql) {
        this.configuration = ms.getConfiguration();
        this.executor = executor;
        this.ms = ms;
        this.paramterObject = paramterObject;
        this.boundSql = boundSql;
        // 初始化结果集处理器
        this.resultSetHandler = configuration.newResultSetHandler(executor,ms,boundSql);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
