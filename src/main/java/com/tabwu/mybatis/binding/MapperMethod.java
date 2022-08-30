package com.tabwu.mybatis.binding;

import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.mapping.SqlCommandType;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/26 14:35
 * @DESCRIPTION: 引射器方法
 */
public class MapperMethod {

    private SqlCommand sqlCommand;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.sqlCommand = new SqlCommand(mapperInterface,method,configuration);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;

        switch (sqlCommand.getSqlCommandType()) {
            case INSERT:
                result = sqlSession.insert(sqlCommand.getName(), args);
                break;
            case DELETE:
                result = sqlSession.delete(sqlCommand.getName(), args);
                break;
            case UPDATE:
                result = sqlSession.update(sqlCommand.getName(), args);
                break;
            case SELECT:
                result = sqlSession.selectOne(sqlCommand.getName(), args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + sqlCommand.getName());
        }

        return result;
    }


    public static class SqlCommand {
        // mappedStatement  id
        private final String name;
        private final SqlCommandType sqlCommandType;

        public SqlCommand(Class<?> mapperInterface, Method method, Configuration configuration) {
            String msId = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(msId);
            this.name = mappedStatement.getId();
            this.sqlCommandType = mappedStatement.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getSqlCommandType() {
            return sqlCommandType;
        }
    }
}
