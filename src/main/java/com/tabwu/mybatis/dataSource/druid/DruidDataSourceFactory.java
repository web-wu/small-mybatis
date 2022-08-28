package com.tabwu.mybatis.dataSource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.tabwu.mybatis.dataSource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 16:26
 * @Description: druid数据源工厂
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties prop;

    @Override
    public void setProperties(Properties property) {
        this.prop = property;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(prop.getProperty("driver"));
        dataSource.setUrl(prop.getProperty("url"));
        dataSource.setUsername(prop.getProperty("username"));
        dataSource.setPassword(prop.getProperty("password"));
        return dataSource;
    }
}
