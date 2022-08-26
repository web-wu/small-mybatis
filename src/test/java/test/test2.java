package test;

import com.tabwu.mybatis.binding.MapperRegistry;
import com.tabwu.mybatis.session.SqlSession;
import com.tabwu.mybatis.session.defaults.DefaultSqlSessionFactory;
import mapper.OrderMapper;
import mapper.UserMapper;
import org.junit.Test;


/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 17:11
 * @DESCRIPTION:
 */
public class test2 {

    @Test
    public void test2() {

        /*MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMapper(UserMapper.class);

        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String res = userMapper.queryUserById(10L);

        System.out.println(res);*/
    }

    @Test
    public void test22() {

        /*MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("mapper");

        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String res = userMapper.queryUserById(10L);

        System.out.println(res);

        System.out.println("===========");

        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        String sd = orderMapper.queryOrderById("assaassasasasa");

        System.out.println(sd);*/
    }
}
