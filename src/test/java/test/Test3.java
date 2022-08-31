package test;

import com.tabwu.mybatis.io.Resources;
import com.tabwu.mybatis.session.SqlSession;
import com.tabwu.mybatis.session.SqlSessionFactory;
import com.tabwu.mybatis.session.SqlSessionFactoryBuilder;
import entity.User;
import mapper.UserMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/8/26 13:38
 * @DESCRIPTION:
 */
public class Test3 {

    @Test
    public void loadConfigFileTest() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryUserById(1);
        System.out.println(user);
    }


    @Test
    public void testResource() {
        InputStream inputStream = Test3.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        System.out.println(inputStream);
    }
}
