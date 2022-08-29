package com.tabwu.mybatis.executor;

import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.ResultHandler;
import com.tabwu.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/29 15:29
 * @DESCRIPTION: 执行器接口
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLE = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
}
