package cn.fbi.common;

import lombok.Data;

import java.util.List;

/**
 *
 * 该类主要用来接收前端在调用处理评论相关请求时传至后端的数据
 * @parm CommentPlus rootComment ,根评论
 * @parm List<CommentPlus> childerComments ,子评论
 * */
@Data
public class CommentParmResult {
    private CommentPlus rootComment;
    private List<CommentPlus> childerComments;
}
