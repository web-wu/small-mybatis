package com.tabwu.mybatis.dataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 16:23
 * @Description: 数据源工厂
 */
public interface DataSourceFactory {

    void setProperties(Properties property);

    DataSource getDataSource();
}
