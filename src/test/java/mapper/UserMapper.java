package mapper;

import entity.User;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 15:28
 * @DESCRIPTION:
 */
public interface UserMapper {

    User queryUserById(Integer id);

    int deleteUserById(Integer id);
}
