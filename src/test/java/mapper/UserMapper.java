package mapper;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/6/16 15:28
 * @DESCRIPTION:
 */
public interface UserMapper {

    String queryUserById(Long id);

    String queryUsernameByAge(int age);
}
