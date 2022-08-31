package com.tabwu.mybatis.builder.xml;

import com.tabwu.mybatis.builder.BaseBuilder;
import com.tabwu.mybatis.io.Resources;
import com.tabwu.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/31 14:21
 * @DESCRIPTION: mapper文件解析构建器
 */
public class XMLMapperBuilder extends BaseBuilder {

    private Element element;

    private String resource;

    private String currentNamespace;

    public XMLMapperBuilder(Configuration configuration, String resource, InputStream inputStream) throws DocumentException {
        this(configuration,new SAXReader().read(inputStream),resource);
    }

    public XMLMapperBuilder(Configuration configuration, Document document, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }


    // 解析
    public void parse() throws ClassNotFoundException {
        // 如果当前资源没有加载过再加载，防止重复加载
        if (!configuration.isLoadedXmlMapper(resource)) {
            configurationElement(element);

            // 标记一下，已经加载过了
            configuration.addXmlMapper(resource);
            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(currentNamespace));
        }
    }


    private void configurationElement(Element element) {
        // 1.配置namespace
        currentNamespace = element.attributeValue("namespace");
        if (currentNamespace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }
        // 2.配置select|insert|update|delete
        buildStatementFromContext(element.elements("insert"));
        buildStatementFromContext(element.elements("delete"));
        buildStatementFromContext(element.elements("update"));
        buildStatementFromContext(element.elements("select"));
    }

    // 配置select|insert|update|delete
    private void buildStatementFromContext(List<Element> list) {
        for (Element node : list) {
            XMLStatementBuilder xmlStatementBuilder = new XMLStatementBuilder(configuration, node, currentNamespace);
            xmlStatementBuilder.parseStatementNode();
        }
    }
}
