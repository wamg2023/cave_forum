package cn.fbi.service.impl;
import cn.fbi.common.LoginUserToken;
import cn.fbi.common.*;
import cn.fbi.entity.*;

import cn.fbi.mapper.CommentMapper;
import cn.fbi.mapper.UserMapper;
import cn.fbi.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
*
* 接口实现类
* */
@Service
public class CommentServiceImpl implements CommentService {
private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
@Autowired
private CommentMapper commentMapper;
@Autowired
private UserMapper userMapper;
@Autowired
private RedisTemplate<String, Object> redisTemplate;

    /** 获取当前登录用户 */
    private User getLoginUser(){
        //获取当前登陆用户
        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
        String currentUserAccount = loginUserToken.getAccount();
        QueryWrapper<User> qwLoginUser =new QueryWrapper<>();
        qwLoginUser.eq("account" , currentUserAccount);
        User user = userMapper.selectOne(qwLoginUser);
        return user;
    }

    /** 生成n位评论ID */
    private String getCommentId(int length) {
        String commentId_str;
        do {
            commentId_str = generateRandomId(length);
        } while (userIdExists(commentId_str));
        return commentId_str;
    }

    /** 根据评论id查找发帖人用户昵称 */
    private String getUserNameById(int comment_id) {
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("comment_id", comment_id);
        Comment comment = commentMapper.selectOne(qw);
        QueryWrapper<User> qw1 = new QueryWrapper<>();
        qw1.eq("user_id", comment.getUser_id());
        User user = userMapper.selectOne(qw1);
        return user.getNickname();
    }

    /** 生成随机的n位数字作为ID */
    private String generateRandomId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0-9的随机数字
        }
        return sb.toString();
    }

    /** 检查生成的commentId是否已存在 */
    private boolean userIdExists(String commentId) {
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("comment_id", commentId);
        return commentMapper.selectCount(qw) > 0;
    }

    /**
     * 获取帖子评论信息方法实现
     */
    public Result getComment(CommentParm commentParm){
        //封装返回信息
        List<CommentPlus> getCommentsData = new ArrayList<>();
        List<CommentParmResult> commentResults = new ArrayList<>();
        //获取评论信息
        int postId = commentParm.getPost_id();
        int sortingMethod = commentParm.getSortingMethod();
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("post_id", postId);
        //根据不同的检索方式，调用不同的查询语句
        if (sortingMethod == 0) {
            qw.orderByDesc("create_time"); // 最新排序
        } else if (sortingMethod == 1) {
            qw.orderByDesc("comment_like_count"); // 最热排序（点赞数）
        } else if (sortingMethod == 2) {
            List<Comment> comments = commentMapper.selectComments(postId); // 只看作者评论
            if (comments.isEmpty()) {
                logger.info("该帖子暂无作者评论");
                return Result.Error("该帖子暂无作者评论");
            } else {
                User user = this.getLoginUser();
                // 获取用户点赞的评论ID字符串
                String likeCommentStr = user.getLike_comment();
                // 将用户点赞过的评论ID转换为列表
                List<String> likeCommentIds = new ArrayList<>();
                if (StringUtils.isNotBlank(likeCommentStr)) {
                    likeCommentIds = Arrays.asList(likeCommentStr.split(","));
                }
                // 设置每条评论的点赞状态
                for (Comment comment : comments) {
                    if (likeCommentIds.contains(Integer.toString(comment.getComment_id()))) {
                        // 标记当前用户已点赞该评论
                        CommentPlus commentPlus = new CommentPlus();
                        commentPlus.setComment(comment);
                        commentPlus.setFlag(true);
                        commentPlus.setComment_owner_nickName(commentMapper.getUserNameById(comment.getUser_id()));
                        getCommentsData.add(commentPlus);
                    } else {
                        // 标记当前用户未点赞该评论
                        CommentPlus commentPlus = new CommentPlus();
                        commentPlus.setComment(comment);
                        commentPlus.setFlag(false);
                        commentPlus.setComment_owner_nickName(commentMapper.getUserNameById(comment.getUser_id()));
                        getCommentsData.add(commentPlus);
                    }
                }
                //将评论按根评论和子评论划分
                for(CommentPlus commentPlus : getCommentsData){
                    if(commentPlus.getComment().getRoot_comment_id()==-1){
                        //存储根评论
                        CommentParmResult commentParmResult = new CommentParmResult();
                        commentParmResult.setRootComment(commentPlus);
                        //寻找并存储子评论
                        List<CommentPlus> childrenComments = new ArrayList<>();
                        for (CommentPlus childComment : getCommentsData){
                            //子评论的根评论id等于根评论的id
                            if(Objects.equals(childComment.getComment().getRoot_comment_id(), commentPlus.getComment().getComment_id()))
                                childrenComments.add(childComment);
                        }
                        commentParmResult.setChilderComments(childrenComments);
                        commentResults.add(commentParmResult);
                    }
                }
                logger.info("帖子评论信息查询成功");
                return Result.Success("评论信息查询成功", commentResults);
            }
        }

        //查看查询结果是否为空，如果为空
        List<Comment> comments = commentMapper.selectList(qw);
        if (comments.isEmpty()) {
            logger.info("无相应评论");
            return Result.Error("无相应评论");
        } else {
            User user = this.getLoginUser();
            // 获取用户点赞的评论ID字符串
            String likeCommentStr = user.getLike_comment();
            // 将用户点赞过的评论ID转换为列表
            List<String> likeCommentIds = new ArrayList<>();
            if (StringUtils.isNotBlank(likeCommentStr)) {
                likeCommentIds = Arrays.asList(likeCommentStr.split(","));
            }
            // 设置每条评论的点赞状态
            for (Comment comment : comments) {
                if (likeCommentIds.contains(Integer.toString(comment.getComment_id()))) {
                    // 标记当前用户已点赞该评论
                    CommentPlus commentPlus = new CommentPlus();
                    commentPlus.setComment(comment);
                    commentPlus.setFlag(true);
                    commentPlus.setComment_owner_nickName(commentMapper.getUserNameById(comment.getUser_id()));
                    getCommentsData.add(commentPlus);
                } else {
                    // 标记当前用户未点赞该评论
                    CommentPlus commentPlus = new CommentPlus();
                    commentPlus.setComment(comment);
                    commentPlus.setFlag(false);
                    commentPlus.setComment_owner_nickName(commentMapper.getUserNameById(comment.getUser_id()));
                    getCommentsData.add(commentPlus);
                }
            }
            //将评论按根评论和子评论划分
            for(CommentPlus commentPlus : getCommentsData){
                if(commentPlus.getComment().getRoot_comment_id()==-1){
                    //存储根评论
                    CommentParmResult commentParmResult = new CommentParmResult();
                    commentParmResult.setRootComment(commentPlus);
                    //寻找并存储子评论
                    List<CommentPlus> childrenComments = new ArrayList<>();
                    for (CommentPlus childComment : getCommentsData){
                        //子评论的根评论id等于根评论的id
                        if(Objects.equals(childComment.getComment().getRoot_comment_id(), commentPlus.getComment().getComment_id()))
                            childrenComments.add(childComment);
                    }
                    commentParmResult.setChilderComments(childrenComments);
                    commentResults.add(commentParmResult);
                }
            }
            logger.info("帖子评论信息查询成功");
            return Result.Success("评论信息查询成功", commentResults);
        }
    }

    /**
     * (回复)评论方法实现
     */
    public Result postComment(Comment comment) {
        String content = comment.getContent();
        int postId = comment.getPost_id();
        int rootCommentId = comment.getRoot_comment_id();
        int toCommentId = comment.getTo_comment_id();
        // 生成10位不重复的id，
        String commentId_str = this.getCommentId(9);
        int commentId = Integer.parseInt(commentId_str);
        // 生成评论时间
        Date createTime = new Date();
        // 根据情况调整评论内容
        if (rootCommentId != toCommentId) {
            // 获取被回复的用户昵称
            String repliedUserName = this.getUserNameById(toCommentId);
            content = "@" + repliedUserName + " " + content;
        }
        // 构造评论对象
        Comment tempcomment = new Comment();
        tempcomment.setComment_id(commentId);
        tempcomment.setContent(content);
        tempcomment.setCreate_time(createTime);
        tempcomment.setUser_id(this.getLoginUser().getUser_id());
        tempcomment.setPost_id(postId);
        tempcomment.setRoot_comment_id(rootCommentId);
        tempcomment.setTo_comment_id(toCommentId);
        // 插入数据库
        commentMapper.insert(tempcomment);
        logger.info("评论成功，评论ID是"+ commentId);
        return Result.Success("评论成功！！！");
    }

    /** 点赞评论 */
    public Result likeComment(Comment comment) {
        int comment_id = comment.getComment_id();
        User user = this.getLoginUser();
        // 获取用户点赞的评论ID字符串
        String likeCommentStr = user.getLike_comment();
        // 将用户点赞过的评论ID转换为列表
        List<String> likeCommentIds = new ArrayList<>();
        if (StringUtils.isNotBlank(likeCommentStr)) {
            likeCommentIds = new ArrayList<>(Arrays.asList(likeCommentStr.split(",")));
        }
        // 判断用户是否已经点赞该评论
        if (!likeCommentIds.contains(Integer.toString(comment.getComment_id()))) {
            // 用户未点赞该评论，将该评论的id加入用户点赞的评论中
            likeCommentIds.add(String.valueOf(comment_id));
            // 更新用户点赞的评论ID字符串
            if (likeCommentIds.isEmpty()) {
                user.setLike_comment(String.valueOf(comment_id));
            } else {
                user.setLike_comment(String.join(",", likeCommentIds));
            }
            //更新用户信息
            UpdateWrapper<User> uw1 = new UpdateWrapper<>();
            uw1.eq("user_id",user.getUser_id());
            userMapper.update(user,uw1);
            // 评论点赞数加一
            comment.setComment_like_count(comment.getComment_like_count() + 1);
            //更新评论信息
            UpdateWrapper<Comment> uw2 = new UpdateWrapper<>();
            uw2.eq("comment_id",comment_id);
            commentMapper.update(comment,uw2);
            logger.info("点赞成功！评论{} 当前的点赞数量为 {}", comment.getComment_id(), comment.getComment_like_count());
            return Result.Success("点赞成功！！！");
        } else {
            // 用户已经点赞该评论，将该评论的id从用户点赞的评论中移除
            likeCommentIds.remove(String.valueOf(comment_id));
            // 更新用户点赞的评论ID字符串
            if (likeCommentIds.isEmpty()) {
                user.setLike_comment("");
            } else {
                user.setLike_comment(String.join(",", likeCommentIds));
            }
            //更新用户信息
            UpdateWrapper<User> uw1 = new UpdateWrapper<>();
            uw1.eq("user_id",user.getUser_id());
            userMapper.update(user,uw1);
            // 评论点赞数减一
            comment.setComment_like_count(comment.getComment_like_count() - 1);
            //更新评论信息
            UpdateWrapper<Comment> uw2 = new UpdateWrapper<>();
            uw2.eq("comment_id",comment_id);
            commentMapper.update(comment,uw2);
            logger.info("取消点赞成功！评论{} 当前的点赞数量为 {}", comment.getComment_id(), comment.getComment_like_count());
            return Result.Success("取消点赞成功！！！");
        }
    }


}



