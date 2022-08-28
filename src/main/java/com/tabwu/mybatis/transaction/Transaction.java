package com.tabwu.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 15:19
 * @Description: 事务接口
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commint() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
