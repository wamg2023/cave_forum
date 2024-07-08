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
    private Integer commentId; // 评论id
    private String content; // 评论内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime; // 评论时间
    private Integer commentLikeCount; // 评论点赞数
    private Integer commentCommentCount; // 评论评论数
    private Integer userId; // 评论的用户id
    private Integer postId; // 评论的帖子id
    private Integer rootCommentId; // 评论的根评论
    private Integer toCommentId; // 评论的回复评论
}
