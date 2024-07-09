package cn.fbi.entity;

import lombok.Data;

@Data
public class EmailVerifyCode {
    private String email;
    private String verify_code;
}
