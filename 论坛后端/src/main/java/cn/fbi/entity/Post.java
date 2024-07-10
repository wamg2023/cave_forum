package cn.fbi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 帖子实体类
 */
@Data
@TableName("post")
public class Post {
    private Integer postId; // 帖子id
    private String title; // 帖子标题
    private String content; // 帖子内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime; // 帖子发布时间
    private String cover; // 帖子封面
    private Integer postLikeCount; // 帖子点赞数
    private Integer postBookmarkCount; // 帖子收藏数
    private Integer postCommentCount; // 帖子评论数
    private String tagId; // 帖子tag
    private Integer userId; // 作者的用户id
}
