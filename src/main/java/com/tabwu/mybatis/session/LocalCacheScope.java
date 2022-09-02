package com.tabwu.mybatis.session;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/9/2 10:32
 * @DESCRIPTION: SESSION 默认值，缓存一个会话中执行的所有查询
 *               STATEMENT 本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不做数据共享
 */
public enum LocalCacheScope {
    SESSION,
    STATEMENT
}
