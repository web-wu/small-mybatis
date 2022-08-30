package com.tabwu.mybatis.executor;

import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.ResultHandler;
import com.tabwu.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/29 15:36
 * @DESCRIPTION: 执行器抽象基类
 */
public abstract class BaseExecutor implements Executor{

    private Configuration configuration;
    protected Transaction transaction;
    private Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }


    @Override
    public int update(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        return doUpdate(ms,parameter,resultHandler,boundSql);
    }


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameter, resultHandler, boundSql);
    }


    protected abstract int doUpdate(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commint();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }
}
