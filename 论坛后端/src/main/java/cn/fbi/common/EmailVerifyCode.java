package cn.fbi.common;

import lombok.Data;

@Data
public class EmailVerifyCode {
    private String email;
    private String verify_code;
}
