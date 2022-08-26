package com.tabwu.mybatis.session;

import com.tabwu.mybatis.builder.xml.XmlConfigBuilder;
import com.tabwu.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:15
 * @DESCRIPTION:  SqlSessionFactory包装类，Configuration初始化和解析配置文件
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
