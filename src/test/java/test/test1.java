package test;

import com.tabwu.mybatis.binding.MapperProxyFactory;
import mapper.UserMapper;
import org.junit.Test;

import java.util.HashMap;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 15:26
 * @DESCRIPTION:
 */
public class test1 {

    /*@Test
    public void Test1() {
        MapperProxyFactory<UserMapper> mapperProxyFactory = new MapperProxyFactory<>(UserMapper.class);

        HashMap<String,String> sqlSession = new HashMap<>();
        sqlSession.put("mapper.UserMapper.queryUserById","UserMapper接口的queryUserById方法被代理了");

        UserMapper userMapper = mapperProxyFactory.newInstance(sqlSession);
        String res = userMapper.queryUserById(11L);
        System.out.println(res);
    }*/

}
