package cn.fbi.service;

import cn.fbi.common.Result;
import cn.fbi.common.CommentParm;
import cn.fbi.entity.Comment;
import org.springframework.stereotype.Service;

/**
 *
 * 评论相关方法的接口类
 * */
@Service
public interface CommentService {
    /** 获取帖子评论信息 */
    Result getComment(CommentParm commentParm);
    /** (回复)评论 */
    Result postComment(Comment comment);
    /** 点赞评论 */
    Result likeComment(Comment comment);
}
