package com.tabwu.mybatis.session.defaults;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.executor.Executor;
import com.tabwu.mybatis.mapping.Environment;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.SqlSession;
import com.tabwu.mybatis.session.SqlSessionFactory;
import com.tabwu.mybatis.session.TransactionIsolationLevel;
import com.tabwu.mybatis.transaction.Transaction;
import com.tabwu.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

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
        Transaction tx =null;
        try {
            Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.REPEATABLE_READ, false);
            // 初始化执行器
            Executor executor = configuration.newExecuttor(tx);

            return new DefaultSqlSession(configuration,executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
