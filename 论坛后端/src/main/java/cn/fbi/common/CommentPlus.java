package cn.fbi.common;

import cn.fbi.entity.Comment;
import lombok.Data;

import java.util.List;

/**
 *
 * 用于存储向前端返回的评论数据类型
 * @Parm  Comment comment,评论信息
 * @Parm boolean flag,为true表示用户当前登录用户已点赞；为false表示未点赞
 * */
@Data
public class CommentPlus {
    private Comment comment;
    private boolean flag;
    private String comment_owner_nickName;

}
