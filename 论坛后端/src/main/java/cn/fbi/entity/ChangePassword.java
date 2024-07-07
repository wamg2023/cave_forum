package cn.fbi.entity;

import lombok.Data;

/**
 * 忘记密码实体类
 * */
@Data
public class ChangePassword {
    private String username;
    private String old_password;
    private String new_password;
    private String again_new_password;
    private String verifyCode;
}
