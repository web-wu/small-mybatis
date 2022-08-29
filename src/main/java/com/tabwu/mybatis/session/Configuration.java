package com.tabwu.mybatis.session;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.dataSource.druid.DruidDataSourceFactory;
import com.tabwu.mybatis.executor.Executor;
import com.tabwu.mybatis.executor.SimpleExecutor;
import com.tabwu.mybatis.executor.resultSet.DefaultResultSetsHandler;
import com.tabwu.mybatis.executor.resultSet.ResultSetHandler;
import com.tabwu.mybatis.executor.statement.PreparedResultHandler;
import com.tabwu.mybatis.executor.statement.StatementHandler;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.Environment;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.transaction.Transaction;
import com.tabwu.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.tabwu.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:20
 * @DESCRIPTION: 配置文件类
 */
public class Configuration {

    //环境
    protected Environment environment;

    //映射器注册机
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    //映射语句，存在map里
    protected final HashMap<String, MappedStatement> statementHashMap = new HashMap<>();

    //类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registryTypeAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registryTypeAlias("DRUID", DruidDataSourceFactory.class);
    }

    public void addMappers(String packgeName) {
        mapperRegistry.addMappers(packgeName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        statementHashMap.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return statementHashMap.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    // 创建结果集处理器
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement ms, BoundSql boundSql) {
        return new DefaultResultSetsHandler(executor,ms,boundSql);
    }

    // 初始化执行器
    public Executor newExecuttor(Transaction transaction) {
        return new SimpleExecutor(this,transaction);
    }

    //创建语句处理器
    public StatementHandler newStatementHandler(Executor executor,MappedStatement ms,Object parameter,ResultHandler resultHandler,BoundSql boundSql) {
        return new PreparedResultHandler(executor,ms,parameter,boundSql);
    }
}
