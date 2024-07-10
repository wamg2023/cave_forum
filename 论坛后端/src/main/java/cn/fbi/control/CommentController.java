package cn.fbi.control;

import cn.fbi.common.Result;
import cn.fbi.common.CommentParm;
import cn.fbi.entity.Comment;
import cn.fbi.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Author:王顺康
 *评论控制器，处理评论相关的HTTP请求
 * */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /** 获取帖子评论信息请求 */
    @GetMapping("/getComment")
    public Result getComment(@RequestBody CommentParm commentParm){
        return commentService.getComment(commentParm);
    }

    /** (回复)评论请求 */
    @PostMapping("/comment")
    public Result postComment(@RequestBody Comment comment){
        return commentService.postComment(comment);
    }

    /** 点赞评论请求 */
    @PostMapping("/likeComment")
    public Result likeComment(@RequestBody Comment comment){
        return commentService.likeComment(comment);
    }

    /** 删除评论请求 */
    @DeleteMapping("/deleteComment")
    public Result DeleteComment(@RequestBody Comment comment){
        return commentService.deleteComment(comment);
    }

}
