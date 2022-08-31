package com.tabwu.mybatis.builder.xml;

import com.tabwu.mybatis.builder.BaseBuilder;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.mapping.SqlCommandType;
import com.tabwu.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/31 14:44
 * @DESCRIPTION: statement 构建器
 */
public class XMLStatementBuilder extends BaseBuilder {

    private Element element;

    private String currentNamespace;

    public XMLStatementBuilder(Configuration configuration, Element element, String currentNamespace) {
        super(configuration);
        this.element = element;
        this.currentNamespace = currentNamespace;
    }


    /*
    <select id="queryUserById" parameterType="java.lang.Integer" resultType="entity.User">
    SELECT id, username, age
    FROM user
    where id = #{id}
    </select>
    */
    public void parseStatementNode() {
        String id = element.attributeValue("id");
        String parameterType = element.attributeValue("parameterType");
        String resultType = element.attributeValue("resultType");
        String sql = element.getText();

        // ? 匹配  替换sql语句
        Map<Integer, String> parameter = new HashMap<>();
        Pattern pattern = Pattern.compile("(#\\{(.*?)})");
        Matcher matcher = pattern.matcher(sql);
        for (int i = 1; matcher.find(); i++) {
            String g1 = matcher.group(1);   // 匹配参数位置 如：#{id}
            String g2 = matcher.group(2);   // 解析参数、去除参数修饰符号 如：#{id} ==> id
            parameter.put(i, g2);           // 封装参数
            sql = sql.replace(g1, "?");   // 将SQL语句中的#{id} 替换为 ？
        }

        String msId = currentNamespace + "." + id;
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        BoundSql boundSql = new BoundSql(parameterType,resultType,sql,parameter);
        MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
        configuration.addMappedStatement(mappedStatement);
    }
}
