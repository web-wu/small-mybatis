package com.tabwu.mybatis.mapping;

import java.util.Map;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 17:35
 * @Description: 绑定的SQL,是从SqlSource而来，将动态内容都处理完成得到的SQL语句字符串，其中包括?,还有绑定的参数
 */
public class BoundSql {
    private String parameterType;
    private String resultType;
    private String sql;
    private Map<Integer, String> parameterMappings;

    public BoundSql(String parameterType, String resultType, String sql, Map<Integer, String> parameterMappings) {
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public BoundSql() {
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, String> getParameterMappings() {
        return parameterMappings;
    }
}
