package cn.fbi.control;

import cn.fbi.common.Result;
import cn.fbi.entity.Post;
import cn.fbi.entity.PostWithUsername;
import cn.fbi.entity.User;
import cn.fbi.service.PostService;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Author:刘航
 * 帖子控制器，处理帖子相关的HTTP请求
 * */
@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    @Resource
    private PostService postService;


    /**
     *  请求帖子的信息
     * */
    @PostMapping("/getPost")
    public Result getPost(@RequestBody Map<String, Object> data){
        String title = (String) data.get("title");
        Integer userType = null;
        Integer tagId = null;
        String sortBy = (String) data.get("sortBy");
        // 检查并转换 userType
        if (data.get("userType") != null) {
            try {
                userType = Integer.valueOf(data.get("userType").toString());
            } catch (NumberFormatException e) {
                // 处理无法转换为整数的情况，例如设置默认值或者抛出异常
                // 这里使用 null，如果需要可以设置一个默认值
            }
        }

        // 检查并转换 tagId
        if (data.get("tagId") != null) {
            try {
                tagId = Integer.valueOf(data.get("tagId").toString());
            } catch (NumberFormatException e) {
                // 处理无法转换为整数的情况，例如设置默认值或者抛出异常
                // 这里使用 null，如果需要可以设置一个默认值
            }
        }

        List<PostWithUsername> posts = postService.getPost(title, userType, tagId, sortBy);
        return Result.Success("请求成功",posts);
    }

    /**
     *  请求某个用户的全部帖子信息
     * */
    @PostMapping("/getPostByUserId")
    public Result getPostsByUserId(@RequestBody Map<String,String> data) {
        int user_id = Integer.parseInt(data.get("userId"));
        try {
            List<PostWithUsername> posts = postService.getPostsByUserId(user_id);
            return Result.Success("查询该用户发布的帖子的全部信息成功",posts);
        } catch (Exception e) {
            return Result.Error("查询失败");
        }
    }


    /**
     *  发布帖子
     * */
    @PostMapping("/postPost")
    public Result postPost(@RequestBody Post post){
        try {
            // 调用服务方法发布帖子
            postService.postPost(post);
            return Result.Success("发帖成功");
        } catch (Exception e) {
            // 可以根据实际情况处理异常，例如记录日志或返回错误信息给前端
            e.printStackTrace();
            return Result.Error("发帖失败");
        }
    }

    /**
     *  PUT请求处理编辑帖子
     * */
    @PostMapping("/editPost")
    public Result editPost(@RequestBody Post post) {

        try {
            // 调用服务方法编辑帖子
            postService.editPost(post);
            return Result.Success("编辑帖子成功");
        } catch (Exception e) {
            // 可以根据实际情况处理异常，例如记录日志或返回错误信息给前端
            e.printStackTrace();
            return Result.Error("编辑帖子失败");
        }
    }


    /**
     *  DELETE请求处理删除帖子
     * */
    @PostMapping("/deletePost")
    public Result deletePost(@RequestBody Post post) {
        try {
            // 调用服务方法删除帖子
            postService.deletePost(post);
            return Result.Success("删除帖子成功");
        } catch (Exception e) {
            // 可以根据实际情况处理异常，例如记录日志或返回错误信息给前端
            e.printStackTrace();
            return Result.Error("删除失败");
        }
    }


    /**
     *  POST请求处理点赞或取消点赞帖子
     * */
    @PostMapping("/likePost")
    public Result togglePostLike(@RequestBody Post post) {

        int postId = post.getPost_id();
        try {
            // 调用服务方法点赞或取消点赞帖子
            postService.togglePostLike(postId);
            return Result.Success("操作成功");
        } catch (Exception e) {
            // 可以根据实际情况处理异常，例如记录日志或返回错误信息给前端
            e.printStackTrace();
            return Result.Error("操作失败");
        }
    }

    /**
     *  POST请求处理收藏或取消收藏帖子
     * */
    @PostMapping("/bookmarkPost")
    public Result togglePostBookmark(@RequestBody Post post) {
        int postId = post.getPost_id();
        postService.togglePostBookmark(postId);
        return Result.Success("操作成功");
    }

    /**
     *  POST请求处理关注或取消关注用户
     * */
    @PostMapping("/followUser")
    public Result followUser(@RequestBody User user) {
        int userId = user.getUser_id();
        postService.followUser(userId);

        return Result.Success("操作成功");
//        return Result.Success("关注成功");
    }

}
