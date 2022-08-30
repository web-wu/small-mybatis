package com.tabwu.mybatis.executor;

import com.tabwu.mybatis.executor.statement.StatementHandler;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.ResultHandler;
import com.tabwu.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/29 15:45
 * @DESCRIPTION: 简单执行器
 */
public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        Configuration configuration = ms.getConfiguration();
        StatementHandler statementHandler = configuration.newStatementHandler(this, ms, parameter,resultHandler,boundSql);
        Connection connection = transaction.getConnection();
        Statement statement = statementHandler.prepare(connection);
        statementHandler.parameterize(statement);
        return statementHandler.update(statement);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            // 初始化语句处理器
            StatementHandler statementHandler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
            Connection connection = transaction.getConnection();
            Statement statement = statementHandler.prepare(connection);
            statementHandler.parameterize(statement);
            return statementHandler.query(statement,resultHandler);
        } catch (SQLException te) {
            te.printStackTrace();
            return null;
        }
    }
}
