package com.tabwu.mybatis.builder.xml;

import com.tabwu.mybatis.builder.BaseBuilder;
import com.tabwu.mybatis.io.Resources;
import com.tabwu.mybatis.mapping.MappedStatement;
import com.tabwu.mybatis.mapping.SqlCommandType;
import com.tabwu.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
            parseMapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void parseMapperElement(Element mappers) throws IOException, DocumentException, ClassNotFoundException {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element mapper : mapperList) {
            String resource = mapper.attributeValue("resource");
            Reader mapperResource = Resources.getResourceAsReader(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(new InputSource(mapperResource));
            Element mapperRoot = document.getRootElement();
            //命名空间
            String namespace = mapperRoot.attributeValue("namespace");

            //SELECT
            List<Element> selects = mapperRoot.elements("select");
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
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, parameterType, resultType, sql, parameter).build();
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}
