package com.tabwu.mybatis.session;

import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 16:10
 * @DESCRIPTION:
 */
public interface SqlSession {

    int insert(String statement,Object parameter);

    int delete(String statement,Object parameter);

    int update(String statement,Object parameter);

    <T> T selectOne(String statement);

    <T> T selectOne(String statement,Object parameter);

    <E> List<E> selectList(String statement,Object parameter);

    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();
}
