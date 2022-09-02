package com.tabwu.mybatis.executor;

import com.tabwu.mybatis.cache.CacheKey;
import com.tabwu.mybatis.cache.impl.PerpetualCache;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.LocalCacheScope;
import com.tabwu.mybatis.session.ResultHandler;
import com.tabwu.mybatis.session.RowBounds;
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
    // 一级缓存
    protected PerpetualCache localCache;
    // 查询堆栈
    protected int queryStack = 0;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
        this.localCache = new PerpetualCache("localCache");
    }


    @Override
    public int update(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 清除缓存
        clearLocalCache();
        return doUpdate(ms,parameter,rowBounds,resultHandler,boundSql);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter,RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        // 生成缓存key
        CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
        return query(ms, parameter, rowBounds,resultHandler, boundSql, key);
    }

    // 查询缓存
    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql, CacheKey key) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        // 清理局部缓存，查询堆栈为0则清理。queryStack 避免递归调用清理
        /*if (queryStack == 0) {
            clearLocalCache();
        }*/
        List<E> list;

        try {
            queryStack++;
            // 根据cacheKey从localCache中查询数据
            list = (List<E>) localCache.getObject(key);
            if (list == null || list.size() == 0) {
                // 缓存不命中时从数据库查询数据
                list = queryFromDataBase(ms,parameter,rowBounds,resultHandler,boundSql,key);
            }
        } finally {
            queryStack--;
        }
        if (queryStack == 0) {
            // 当一级缓存作用域localCacheScope 为 STATEMENT 时不缓存数据
            if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
                clearLocalCache();
            }
        }
        return list;
    }

    private  <E> List<E> queryFromDataBase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql, CacheKey key) {
        List<E> list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
        // 存入缓存
        localCache.putObject(key,list);
        return list;
    }


    protected abstract int doUpdate(MappedStatement ms, Object parameter, RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql);

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
        // 清除缓存
        clearLocalCache();

        if (required) {
            transaction.commint();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            // 清除缓存
            clearLocalCache();

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


    @Override
    public void clearLocalCache() {
        if (!closed) {
            localCache.clear();
        }
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(ms.getId());
        cacheKey.update(rowBounds.getOffset());
        cacheKey.update(rowBounds.getLimit());
        cacheKey.update(boundSql.getSql());
        // 此处简化参数处理, 直接将参数传入
        cacheKey.update(parameterObject);
       /* List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
        for (ParameterMapping parameterMapping : parameterMappings) {
            Object value;
            String propertyName = parameterMapping.getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (parameterObject == null) {
                value = null;
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                value = metaObject.getValue(propertyName);
            }
            cacheKey.update(value);
        }*/

        if (configuration.getEnvironment() != null) {
            cacheKey.update(configuration.getEnvironment().getId());
        }
        return cacheKey;
    }
}
