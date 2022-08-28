package com.tabwu.mybatis.mapping;

import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.type.JdbcType;


/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 17:24
 * @Description: 参数映射 #{property,javaType=int,jdbcType=NUMERIC}
 */
public class Parametermapping {

    private Configuration configuration;

    private String property;

    private Class<?> javaType;

    private JdbcType jdbcType;

    public Parametermapping() {
    }

    public static class Builder {
        private Parametermapping parametermapping = new Parametermapping();

        public Builder(Configuration configuration, String property) {
            parametermapping.configuration = configuration;
            parametermapping.property = property;
        }

        public Builder javaType(Class<?> javaType) {
            parametermapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parametermapping.jdbcType = jdbcType;
            return this;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

}
