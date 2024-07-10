package cn.fbi.mapper;

import cn.fbi.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 自定义方法：检查postID是否存在
     * */
    @Select("SELECT COUNT(*) > 0 FROM post WHERE post_id = #{postId}")
    boolean existsPostId(@Param("postId") int postId);

    /**
     * 查询用户点赞的帖子ID列表
     * */
    @Select("SELECT like_post FROM user WHERE account = #{account}")
    String findLikedPosts(@Param("account") String account);


    /**
     * 更新用户点赞的帖子ID列表（添加新的帖子ID）
     * */
    @Update("UPDATE user SET like_post = #{likePostIds} WHERE account = #{account}")
    void setLikedPosts(@Param("likePostIds") String likePostIds, @Param("account") String account);


    /**
     *  增加帖子的点赞数
     * */
    @Update("UPDATE post SET post_like_count = post_like_count + 1 WHERE post_id = #{postId}")
    void incrementPostLikeCount(@Param("postId") int postId);

    /**
     *  减少帖子的点赞数
     * */
    @Update("UPDATE post SET post_like_count = post_like_count - 1 WHERE post_id = #{postId}")
    void decrementPostLikeCount(@Param("postId") int postId);

    /**
     *  查询用户收藏的帖子ID列表
     * */
    @Select("SELECT bookmark_post FROM user WHERE account = #{account}")
    String findBookmarkedPost(@Param("account") String account);

    /**
     *  更新用户收藏的帖子ID列表（添加新的帖子ID）
     * */
    @Update("UPDATE user SET bookmark_post = #{BookmarkPostIds} WHERE account = #{account}")
    void setBookmarkPosts(@Param("BookmarkPostIds") String BookmarkPostIds, @Param("account") String account);

    /**
     *  增加帖子的收藏数
     * */
    @Update("UPDATE post SET post_bookmark_count = post_bookmark_count + 1 WHERE post_id = #{postId}")
    void incrementPostBookmarkCount(@Param("postId") int postId);

    /**
     *  减少帖子的收藏数
     * */
    @Update("UPDATE post SET post_bookmark_count = post_bookmark_count - 1 WHERE post_id = #{postId}")
    void decrementPostBookmarkCount(@Param("postId") int postId);

    /**
     *  查询用户关注的用户ID列表
     * */
    @Select("SELECT follow_user FROM user WHERE account = #{account}")
    String getFollowUser(@Param("account") String account);

    /**
     *  更新用户关注的用户ID列表
     * */
    @Update("UPDATE user SET follow_user = #{followUsers} WHERE account = #{account}")
    void setFollowUser(@Param("followUsers") String followUsers, @Param("account") String account);

    /**
     *  查询对用用户类型的用户id
     * */
    @Select("SELECT user_id FROM user WHERE type = #{userType}")
    List<Long> findUserIdsByUserType(@Param("userType") Integer userType);

    /**
     *  查询用户id相同的用户名
     * */
    @Select("SELECT nickname FROM user WHERE user_id = #{userId}")
    String getUsernameByUserId(@Param("userId") int userId);
}
