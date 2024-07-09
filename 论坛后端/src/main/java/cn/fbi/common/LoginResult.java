package cn.fbi.common;

import cn.fbi.entity.User;
/**
 *
 * 登录信息返回数据类型
 * @Parm token 请求头
 * @Parm user 用户信息
 * */
public class LoginResult {
    private String Token;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
