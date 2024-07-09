package cn.fbi.entity;

import lombok.Data;

@Data
public class LoginUserToken {
    private String account;//用户账号
    private String email;//用户邮箱
    private String token;//请求头
}
