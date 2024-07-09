package cn.fbi.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 用户实体类
 * userid,十位数组组成的账号
 * type，用户类型，0是管理员，1是用户
 * 由于idea及数据库关于命名规则不同的原因，2024 7 7 17：20做如下修改：
 * 将原followUser字段更名为,follower_user
 * 将原likePost字段更名为,like_post
 * 将原bookMarkPost字段更名为,book_mark_post
 * 将原likeComment字段更名为,like_comment
 * 将原verifyCode字段更名为,verify_code
 * */
@Data
public class User {
    private int user_id;//用户id
    private Integer type;//用户类型
    private String account;//账号
    private String password;//用户密码
    private String nickname;//用户昵称
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;//用户出生年月日
    private String email;//用户邮箱
    private String avatar;//用户头像
    private String follow_user;//关注的用户
    private String like_post;//点赞的帖子
    private String bookmark_post;//收藏的帖子
    private String like_comment;//点赞的评论
    private String verify_code;//验证码
}
