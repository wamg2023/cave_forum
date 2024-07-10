package cn.fbi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {
    private int comment_id; // 评论id
    private String content; // 评论内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date create_time; // 评论时间
    private int comment_like_count; // 评论点赞数
    private int comment_comment_count; // 评论评论数
    private int user_id; // 评论的用户id
    private int post_id; // 评论的帖子id
    private int root_comment_id; // 评论的根评论
    private int to_comment_id; // 评论的回复评论
}
