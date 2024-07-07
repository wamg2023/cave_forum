package cn.fbi.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 用户实体类
 * userid,十位数组组成的账号
 * type，用户类型，0是管理员，1是用户
 * */
@Data
@TableName("user")
public class User {
    private Integer userid;//用户id
    private Integer type;//用户类型
    private String account;//账号
    private String password;//用户密码
    private String nickname;//用户昵称
    private Date birthday;//用户出生年月日
    private String email;//用户邮箱
    private String avatar;//用户头像
    private String followUser;//关注的用户
    private String likePost;//点赞的帖子
    private String bookMarkPost;//收藏的帖子
    private String likeComment;//点赞的评论
    private String verifyCode;//验证码
}
