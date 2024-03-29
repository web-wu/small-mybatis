package test;

import com.tabwu.mybatis.io.Resources;
import com.tabwu.mybatis.session.SqlSession;
import com.tabwu.mybatis.session.SqlSessionFactory;
import com.tabwu.mybatis.session.SqlSessionFactoryBuilder;
import mapper.UserMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * @ProjectName: small-mybatis
 * @Author: tabwu
 * @Date: 2022/8/27 18:08
 * @Description:
 */
public class LoadDataSourceTest {


    @Test
    public void testDataSource() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.queryUserById(123456789);
    }
}
