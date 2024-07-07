package cn.fbi.service;

import cn.fbi.entity.Post;

import java.util.List;

/**
 *
 * 帖子相关方法的接口类
 * */
public interface PostService {

    List<Post> getPost(String searchTerm, String userId, String tagId, String sortBy);

    void postPost(String title, String content, String tag, String coverImage);
}
