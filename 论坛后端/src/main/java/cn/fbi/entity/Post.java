package cn.fbi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.ResultSet;
import java.util.Date;

/**
 * 帖子实体类
 */
@Data
@TableName("post")
public class Post {

    private int post_id; // 帖子id
    private String title; // 帖子标题
    private String content; // 帖子内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date create_time; // 帖子发布时间
    private String cover; // 帖子封面
    private int post_like_count; // 帖子点赞数
    private int post_bookmark_count; // 帖子收藏数
    private int post_comment_count; // 帖子评论数
    private int tag_id; // 帖子tag
    private int user_id; // 作者的用户id


}
