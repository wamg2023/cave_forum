package cn.fbi.service;

import cn.fbi.common.Result;
import cn.fbi.entity.Post;
import cn.fbi.entity.PostWithUsername;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 *
 * 帖子相关方法的接口类
 * */
public interface PostService {

    /**
     * 获取帖子信息
     * */
    List<PostWithUsername> getPost(String title, Integer userType, Integer tagId, String sortBy);
    /**
     * 获取某个用户的全部帖子信息
     * */
    List<PostWithUsername> getPostsByUserId(int userId);
    /**
     *发布帖子
     * */
    Result postPost(Post post);
    /**
     *编辑帖子
     * */
    Result editPost(Post post);
    /**
     *删除帖子
     * */
    void deletePost(Post post);
    /**
     *点赞或者取消点赞
     * */
    Result togglePostLike(int postId);
    /**
     *收藏或者取消收藏
     * */
    Result togglePostBookmark(int postId);
    /**
     *关注或者取消关注
     * */
    Result followUser(int followedUserId);
}
