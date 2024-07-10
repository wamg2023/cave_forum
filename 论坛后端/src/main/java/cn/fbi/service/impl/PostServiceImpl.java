package cn.fbi.service.impl;


/**
 *
 * 接口实现类
 * */
import cn.fbi.common.CommentPlus;
import cn.fbi.common.Result;
import cn.fbi.common.LoginUserToken;
import cn.fbi.entity.Comment;
import cn.fbi.entity.Post;
import cn.fbi.entity.PostWithUsername;
import cn.fbi.entity.User;
import cn.fbi.helper.UniqueIdGenerator;
import cn.fbi.mapper.PostMapper;
import cn.fbi.mapper.UserMapper;
import cn.fbi.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
    * 通过模糊查询获取帖子信息
    * */
    @Override
    public List<PostWithUsername> getPost(String title, Integer userType, Integer tagId, String sortBy) {
//        if (title != null) {
//            // 根据标题查询帖子
//            List<Post> posts = postMapper.findPostsByTitle(title);
//            return filterAndSortPosts(posts, userType, tagId, sortBy);
//        } else if (userType == 0) {
//            // 根据发布者类别查询帖子
//            List<Post> posts = postMapper.findPostsByUserType(userType);
//            return filterAndSortPosts(posts, null, tagId, sortBy);
//        } else if (userType == 1) {
//            // 根据发布者类别查询帖子
//            List<Post> posts = postMapper.findPostsByUserType(userType);
//            return filterAndSortPosts(posts, null, tagId, sortBy);
//        } else if (tagId != null) {
//            // 根据标签查询帖子
//            List<Post> posts = postMapper.findPostsByTag(tagId);
//            return filterAndSortPosts(posts, null, null, sortBy);
//        } else {
//            // 查询所有帖子
//            List<Post> posts = postMapper.findAllPosts();
//            System.out.println(posts);
//            return filterAndSortPosts(posts, null, null, sortBy);
//        }
        QueryWrapper<Post> qw = new QueryWrapper<>();

        // 添加查询条件
        if (title != null && !title.isEmpty()) {
            qw.like("title", title);
        }

        // 使用联表查询筛选帖子
        if (userType != null) {
            // Step 1: 根据 userType 找到符合条件的 user_id 列表
            List<Long> userIds = postMapper.findUserIdsByUserType(userType);

            // Step 2: 根据 user_id 列表查询符合条件的帖子
            qw.in("user_id", userIds);
        }

        if (tagId != null) {
            qw.eq("tag_id", tagId);
        }

        // 执行查询
        List<Post> posts = postMapper.selectList(qw);

        // 构建帖子和用户名的复合对象列表
        List<PostWithUsername> postsWithUsernames = buildPostsWithUsernames(posts);

        // 排序复合对象列表
        postsWithUsernames = sortPostsWithUsernames(postsWithUsernames, sortBy);
//        // 排序
//        posts = sortPosts(posts, sortBy);

        return postsWithUsernames;
    }

    /**
     *通过查询用户id得到对应用户的帖子
     * */
    @Override
    public List<PostWithUsername> getPostsByUserId(int userId) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 执行查询，获取帖子列表
        List<Post> posts = postMapper.selectList(queryWrapper);

        // 构建帖子和用户名的复合对象列表
        List<PostWithUsername> postsWithUsernames = buildPostsWithUsernames(posts);

        return postsWithUsernames;
    }

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

    /**
     * 构建一个存在post和username的复合对象
     * */
    private List<PostWithUsername> buildPostsWithUsernames(List<Post> posts) {
        List<PostWithUsername> result = new ArrayList<>();
        User user = this.getLoginUser();
        String likePostStr = user.getLike_post();
        String bookmarkPostStr = user.getBookmark_post();
        String followUserStr = user.getFollow_user();

        List<String> likePostIds = new ArrayList<>();
        List<String> bookmarkPostIds = new ArrayList<>();
        List<String> followUserIds = new ArrayList<>();
        if (StringUtils.isNotBlank(likePostStr)) {
            likePostIds = Arrays.asList(likePostStr.split(","));
        }
        if (StringUtils.isNotBlank(bookmarkPostStr)) {
            bookmarkPostIds = Arrays.asList(bookmarkPostStr.split(","));
        }
        if (StringUtils.isNotBlank(followUserStr)) {
            followUserIds = Arrays.asList(followUserStr.split(","));
        }

        for (Post post : posts) {
            System.out.println(post);
            boolean likePostFlag = likePostIds.contains(Integer.toString(post.getPost_id()));
            boolean bookmarkPostFlag = bookmarkPostIds.contains(Integer.toString(post.getPost_id()));
            boolean followUserFlag = followUserIds.contains(Integer.toString(post.getUser_id()));

            String username = postMapper.getUsernameByUserId(post.getUser_id());
            PostWithUsername postWithUsername = new PostWithUsername(post, username, likePostFlag, bookmarkPostFlag, followUserFlag);
            result.add(postWithUsername);
//            String username = postMapper.getUsernameByUserId(post.getUser_id());
//            PostWithUsername postWithUsername = new PostWithUsername(post, username, flag);
        }
        return result;
    }

    /**
     * 对复合对象进行排序
     * */
    private List<PostWithUsername> sortPostsWithUsernames(List<PostWithUsername> postsWithUsernames, String sortBy) {
        if ("hot".equalsIgnoreCase(sortBy)) {
            postsWithUsernames.sort((p1, p2) -> Integer.compare(p2.getPost().getPost_like_count(), p1.getPost().getPost_like_count())); // 按点赞数从多到少排序
        } else {
            // 默认按时间排序或者处理 sortBy 为空或其他情况
            postsWithUsernames.sort((p1, p2) -> p2.getPost().getCreate_time().compareTo(p1.getPost().getCreate_time())); // 按时间从最近到最早排序
        }

        // 可以根据其他条件继续添加排序方式

        return postsWithUsernames;
    }

//    private List<Post> sortPosts(List<Post> posts, String sortBy) {
//        if ("hot".equalsIgnoreCase(sortBy)) {
//            posts.sort((p1, p2) -> Integer.compare(p2.getPost_like_count(), p1.getPost_like_count())); // 按点赞数从多到少排序
//        } else {
//            // 默认按时间排序或者处理 sortBy 为空或其他情况
//            posts.sort((p1, p2) -> p2.getCreate_time().compareTo(p1.getCreate_time())); // 按时间从最近到最早排序
//        }
//
//        return posts;
//    }
//    private List<Post> filterAndSortPosts(List<Post> posts, Integer userType, Integer tagId, String orderBy) {
//        // 过滤发布者类别
//        if (userType != null) {
//            posts = posts.stream().filter(post -> {
//                System.out.println(post);
//                return post.getUser().getType() == userType;
//            }).collect(Collectors.toList());
//        }
//        // 过滤标签
//        if (tagId != null) {
//            posts = posts.stream().filter(post -> post.getTag_id() == tagId).collect(Collectors.toList());
//        }
//        // 排序
//        if ("hot".equals(orderBy)) {
//            posts.sort((p1, p2) -> Integer.compare(p2.getPost_like_count(), p1.getPost_like_count())); // 按点赞数从多到少排序
//        } else if ("time".equals(orderBy)) {
//            posts.sort((p1, p2) -> p2.getCreate_time().compareTo(p1.getCreate_time())); // 按时间从最近到最早排序
//        }
//        return posts;
//    }

    /**
     *发布帖子
     * */
    @Override
    public Result postPost(Post post) {
        //生成8位不重复的帖子id
        int postId = generateUniqueId();
        //如果有重复的id则重新生成，知道没有为止
        while (postMapper.existsPostId(postId)){
            postId = generateUniqueId();
        }

        String coverImage = postId + "Cover.png";
        String title = post.getTitle();
        String content = post.getContent();
        int tag = post.getTag_id();

        //获取当前时间
        LocalDateTime createTime = LocalDateTime.now();

        //把LocalDateTime转换成Date类型
        Date postTime = Date.valueOf(createTime.toLocalDate());

        int postLikeCount = 0;
        int postBookmarkCount = 0;
        int postCommentCount = 0;

        //创建一个帖子对象
        Post posts = new Post();
        posts.setPost_id(postId);
        posts.setTitle(title);
        posts.setContent(content);
        posts.setTag_id(tag);
        posts.setCover(coverImage);
        posts.setCreate_time(postTime);
        posts.setPost_like_count(postLikeCount);
        posts.setPost_bookmark_count(postBookmarkCount);
        posts.setPost_comment_count(postCommentCount);

        //把post的数据插入到数据库里面
        int rowsAffected = postMapper.insert(posts);

        // 判断插入是否成功
        if (rowsAffected>0) {
            return Result.Success("200，发帖成功");
            // 可以抛出自定义异常或者返回错误信息
        } else {
            return Result.Error("400，发帖失败");
            // 可以抛出自定义异常或者返回错误信息
        }

    }


    /**
     *编辑帖子
     * */
    @Override
    public Result editPost(Post post) {
        // 获取当前时间
        LocalDateTime editTime = LocalDateTime.now();
        // 转换成 Date 类型
        Date postTime = Date.valueOf(editTime.toLocalDate());

        int postId = post.getPost_id();
        String title = post.getTitle();
        String content = post.getContent();
        int tag = post.getTag_id();

        post.setTitle(title);
        post.setContent(content);
        post.setTag_id(tag);

        // 更新帖子信息
        UpdateWrapper<Post> uw1 = new UpdateWrapper<>();
        uw1.eq("post_id",post.getPost_id());
        postMapper.update(post,uw1);


        // 可以根据需要记录日志或返回其他信息
        return Result.Success("编辑成功！");
    }

    /**
     *删除帖子
     * */
    @Override
    public void deletePost(Post post) {
        int postId = post.getPost_id();
        UpdateWrapper<Post> uw1 = new UpdateWrapper<>();
        uw1.eq("post_id",postId);
        postMapper.delete(uw1);
        // 调用 MyBatis Plus 的删除方法
        //postMapper.deleteById(postId);

        // 可以根据需要记录日志或返回其他信息
        System.out.println("删除帖子成功");
    }

    /**
     *点赞或者取消点赞
     * */
    @Override
    public Result togglePostLike(int postId) {
        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
        String account = loginUserToken.getAccount();

        String likedPosts = postMapper.findLikedPosts(account);
        List<Integer> likedIds;

        // 判断 likedPosts 是否为空
        if (likedPosts.equals("null")|| likedPosts.isEmpty()) {
            // 如果 likedPosts 为空，初始化一个空列表
            likedIds = new ArrayList<>();
        } else {
            // 如果不为空，解析 likedPosts 获取点赞的帖子列表
            likedIds = parseIds(likedPosts);
        }

        // 根据用户已有的点赞状态更新 likedIds
        if (likedIds.contains(postId)) {
            // 获取 postId 在列表中的索引
            int index = likedIds.indexOf(postId);
            likedIds.remove(index);
        } else {
            likedIds.add(postId);
        }

        // 更新用户点赞的帖子列表
        String likedPostIds = joinIds(likedIds);
        postMapper.setLikedPosts(likedPostIds, account);

        // 根据点赞状态增加或减少帖子的点赞数
        if (likedIds.contains(postId)) {
            postMapper.incrementPostLikeCount(postId);
            System.out.println("点赞成功");
        } else {
            postMapper.decrementPostLikeCount(postId);
            System.out.println("取消点赞成功");
        }

//        if (likedIds.contains(postId)) {
//            // 用户已经点赞过，取消点赞
//            likedIds.remove(postId);
//            String likedPostIds = joinIds(likedIds);
//            postMapper.setLikedPosts(likedPostIds, account);
//            postMapper.decrementPostLikeCount(postId);
//            System.out.println("取消点赞成功");
//        } else {
//            // 用户未点赞过，点赞
//            likedIds.add(postId);
//            String likedPostIds = joinIds(likedIds);
//            postMapper.setLikedPosts(likedPostIds, account);
//            postMapper.incrementPostLikeCount(postId);
//            System.out.println("点赞成功");
//        }
        return Result.Success("点赞成功！");
    }

    /**
     *收藏帖子
     * */
    @Override
    public Result togglePostBookmark(int postId) {
        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
        String account = loginUserToken.getAccount();

        String bookmarkPosts = postMapper.findBookmarkedPost(account);
        List<Integer> bookmarkIds;

        // 判断 bookmarkPosts 是否为空
        if (bookmarkPosts.equals("null") || bookmarkPosts.isEmpty()) {
            // 如果 bookmarkPosts 为空，初始化一个空列表
            bookmarkIds = new ArrayList<>();
        } else {
            // 如果不为空，解析 bookmarkPosts 获取点赞的帖子列表
            bookmarkIds = parseIds(bookmarkPosts);
        }

        // 根据用户已有的收藏状态更新 bookmarkIds
        if (bookmarkIds.contains(postId)) {
            // 获取 postId 在列表中的索引
            int index = bookmarkIds.indexOf(postId);
            bookmarkIds.remove(index);
        } else {
            bookmarkIds.add(postId);
        }

        // 更新用户收藏的帖子列表
        String bookmarkedPostIds = joinIds(bookmarkIds);
        postMapper.setBookmarkPosts(bookmarkedPostIds, account);

        // 根据收藏状态增加或减少帖子的收藏数
        if (bookmarkIds.contains(postId)) {
            postMapper.incrementPostBookmarkCount(postId);
            System.out.println("收藏成功");
        } else {
            postMapper.decrementPostBookmarkCount(postId);
            System.out.println("取消收藏成功");
        }
//        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
//
//        String account = loginUserToken.getAccount();
//        String bookmarkPosts = postMapper.findBookmarkedPost(account);
//        List<Integer> bookmarkIds = parseIds(bookmarkPosts);
//
//        if (bookmarkIds.contains(postId)) {
//            // 用户已经收藏过，取消收藏
//            bookmarkIds.remove(postId);
//            String BookmarkPostIds = joinIds(bookmarkIds);
//            postMapper.setBookmarkPosts(BookmarkPostIds, account);
//            postMapper.decrementPostBookmarkCount(postId);
//            System.out.println("取消收藏成功");
//        } else {
//            // 用户未收藏过，收藏
//            bookmarkIds.add(postId);
//            String BookmarkPostIds = joinIds(bookmarkIds);
//            postMapper.setBookmarkPosts(BookmarkPostIds, account);
//            postMapper.incrementPostBookmarkCount(postId);
//            System.out.println("收藏成功");
//        }'
        return Result.Success("收藏成功");
    }

    /**
     *关注用户
     * */
    @Override
    public Result followUser(int followedUserId) {

        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
        String account = loginUserToken.getAccount();

        String followUsers = postMapper.getFollowUser(account);
        List<Integer> followUserIds;

        // 判断 followUsers 是否为空
        if (followUsers.equals("null") || followUsers.isEmpty()) {
            // 如果 followUsers 为空，初始化一个空列表
            followUserIds = new ArrayList<>();
        } else {
            // 如果不为空，解析 followUsers 获取关注的用户列表
            followUserIds = parseIds(followUsers);
        }

        // 根据用户已有的关注状态更新 followUserIds
        if (followUserIds.contains(followedUserId)) {
            // 获取 postId 在列表中的索引
            int index = followUserIds.indexOf(followedUserId);
            followUserIds.remove(index);
        } else {
            followUserIds.add(followedUserId);
        }

        // 更新用户关注的帖子列表
        String bookmarkedPostIds = joinIds(followUserIds);
        postMapper.setFollowUser(bookmarkedPostIds, account);

//
//        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
//
//        String account = loginUserToken.getAccount();
////        User user = userMapper.selectById(userId);
////        if (user == null) {
////            return "用户不存在";
////        }
//
//        String followedUserIds = postMapper.getFollowUser(account);
//        List<Integer> followedIds = parseIds(followedUserIds);
//
//        if (followedIds.contains(followedUserId)) {
//            // 如果已经关注，取消关注
//            followedIds.remove(followedUserId);
//            String followUsers = joinIds(followedIds);
//            postMapper.setFollowUser(followUsers, account);
//            return "取消关注成功";
//        } else {
//            // 如果未关注，关注
//            followedIds.add(followedUserId);
//            String followUsers = joinIds(followedIds);
//            postMapper.setFollowUser(followUsers, account);
//            return "关注成功";
//        }
        return Result.Success("操作成功！");
    }


    /**
     *用于生成帖子的id
     * */
    private Integer generateUniqueId() {
        //随机生成一个8位不重复的id
        return UniqueIdGenerator.generateUniqueId();
    }

    /**
     *辅助方法：将逗号分隔的字符串转换为列表
     * */
    private List<Integer> parseIds(String Ids) {
        List<Integer> ids = new ArrayList<>();
        if (Ids != null && !Ids.isEmpty()) {
            String[] idStrings = Ids.split(",");
            for (String idString : idStrings) {
                ids.add(Integer.valueOf(idString));
            }
        }
        return ids;
    }

    /**
     *辅助方法：将列表转换为逗号分隔的字符串
     * */
    private String joinIds(List<Integer> Ids) {
        StringJoiner joiner = new StringJoiner(",");
        for (int id : Ids) {
            joiner.add(String.valueOf(id));
        }
        return joiner.toString();
    }

}
