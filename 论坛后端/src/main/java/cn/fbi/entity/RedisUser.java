package cn.fbi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RedisUser implements Serializable {
    private String username;
    private String password;
}
