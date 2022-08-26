package com.tabwu.mybatis.session;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 16:10
 * @DESCRIPTION:
 */
public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement,Object parameter);

    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();
}
