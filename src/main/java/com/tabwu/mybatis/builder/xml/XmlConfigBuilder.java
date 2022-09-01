package com.tabwu.mybatis.builder.xml;

import com.tabwu.mybatis.builder.BaseBuilder;
import com.tabwu.mybatis.dataSource.DataSourceFactory;
import com.tabwu.mybatis.io.Resources;
import com.tabwu.mybatis.mapping.BoundSql;
import com.tabwu.mybatis.mapping.Environment;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.mapping.SqlCommandType;
import com.tabwu.mybatis.plugin.Interceptor;
import com.tabwu.mybatis.session.Configuration;
import com.tabwu.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/25 17:20
 * @DESCRIPTION: 根据配置文件输入流解析XML并填充Configuration
 */
public class XmlConfigBuilder extends BaseBuilder {
    private Element root;

    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());
        SAXReader readerDom = new SAXReader();
        try {
            Document document = readerDom.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     *
     * @return Configuration
     */
    public Configuration parse() {
        try {
            //解析插件 plugin
            parsePluginElement(root.element("plugins"));

            // 解析数据源环境
            parseEnvironmentsElement(root.element("environments"));

            // 解析mapper映射文件
            parseMapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void parsePluginElement(Element pluginsEle) throws IllegalAccessException, InstantiationException {
        if (pluginsEle == null) {
            return ;
        }
        List<Element> plugins = pluginsEle.elements("plugin");
        for (Element plugin : plugins) {
            String interceptorClass = plugin.attributeValue("interceptor");
            if (!"".equalsIgnoreCase(interceptorClass)) {
                Properties properties = new Properties();
                List<Element> propertyList = plugin.elements("property");
                for (Element pro : propertyList) {
                    properties.setProperty(pro.attributeValue("name"), pro.attributeValue("value"));
                }

                Interceptor interceptor = (Interceptor) resolveClass(interceptorClass).newInstance();
                interceptor.setProperties(properties);
                configuration.addInterceptor(interceptor);
            }
        }
    }

    /*
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="DRUID">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    */
    private void parseEnvironmentsElement(Element context) throws IllegalAccessException, InstantiationException {
        String id = context.attributeValue("default");
        List<Element> environment = context.elements("environment");
        for (Element element : environment) {
            if (id.equals(element.attributeValue("id"))) {
                // 事务管理器 事务工厂
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.getAliasType(element.element("transactionManager").attributeValue("type")).newInstance();
                // 数据源工厂
                Element dataSourceEle = element.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.getAliasType(dataSourceEle.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceEle.elements("property");
                Properties properties = new Properties();
                for (Element pro : propertyList) {
                    properties.setProperty(pro.attributeValue("name"), pro.attributeValue("value"));
                }
                // 数据源
                dataSourceFactory.setProperties(properties);
                DataSource dataSource = dataSourceFactory.getDataSource();

                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    /*
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     * </mappers>
     */
    private void parseMapperElement(Element mappers) throws IOException, DocumentException, ClassNotFoundException {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element mapper : mapperList) {
            String resource = mapper.attributeValue("resource");


            InputStream inputStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration, resource, inputStream);
            xmlMapperBuilder.parse();

            /*
            Reader mapperResource = Resources.getResourceAsReader(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(new InputSource(mapperResource));
            Element mapperRoot = document.getRootElement();
            //命名空间
            String namespace = mapperRoot.attributeValue("namespace");

            //INSERT
            parseSelectNode(namespace, mapperRoot.elements("insert"));
            //DELETE
            parseSelectNode(namespace, mapperRoot.elements("delete"));
            //UPDATE
            parseSelectNode(namespace, mapperRoot.elements("update"));
            //SELECT
            parseSelectNode(namespace, mapperRoot.elements("select"));

            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
            */
        }
    }

   /*
   private void parseSelectNode(String namespace, List<Element> selects) {
        for (Element selectNode : selects) {
            String id = selectNode.attributeValue("id");
            String parameterType = selectNode.attributeValue("parameterType");
            String resultType = selectNode.attributeValue("resultType");
            String sql = selectNode.getText();

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

            String msId = namespace + "." + id;
            String nodeName = selectNode.getName();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

            BoundSql boundSql = new BoundSql(parameterType,resultType,sql,parameter);
            MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
            configuration.addMappedStatement(mappedStatement);
        }
    }
    */

}
