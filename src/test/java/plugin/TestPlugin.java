package plugin;

import com.tabwu.mybatis.executor.statement.StatementHandler;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.plugin.Interceptor;
import com.tabwu.mybatis.plugin.Intercepts;
import com.tabwu.mybatis.plugin.Invocation;
import com.tabwu.mybatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/9/1 11:17
 * @DESCRIPTION:   插件测试
 */
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class})})
public class TestPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        System.out.println("将要执行sql==> " + boundSql.getSql());
        return invocation.proceed();
    }


    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件参数： " + properties.getProperty("dbType"));
    }
}
