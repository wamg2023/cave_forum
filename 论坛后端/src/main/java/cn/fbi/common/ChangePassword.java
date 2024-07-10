package cn.fbi.common;

import lombok.Data;

/**
 * 忘记密码实体类
 * */
@Data
public class ChangePassword {
    private String account;//用户账号
    private String oldPassword;//旧密码
    private String newPassword;//新密码
    private String email;//用户邮箱
    private String captcha;//验证码
}
