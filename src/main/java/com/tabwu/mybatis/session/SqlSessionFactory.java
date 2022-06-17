package com.tabwu.mybatis.session;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 16:14
 * @DESCRIPTION:
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
