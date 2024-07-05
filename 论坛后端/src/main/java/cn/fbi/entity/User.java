package cn.fbi.entity;
import lombok.Data;


/**
 * 用户实体类
 * */
@Data
public class User {
    private Integer id;
    //账号
    private String username;
    //密码
    private String password;
    //年龄
    private Integer age;
    //性别
    private String sex;
    //邮箱
    private String email;

}
