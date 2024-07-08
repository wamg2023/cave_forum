package cn.fbi.control;

import cn.fbi.entity.Post;
import cn.fbi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Author:刘航
 * 帖子控制器，处理帖子相关的HTTP请求
 * */
@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;


    //请求贴则的信息
    @PostMapping("/getPost")
    public List<Post> getPost(@RequestBody String searchTerm, String userId, String tagId, String sortBy){
        List<Post> posts = postService.getPost(searchTerm, userId, tagId, sortBy);
        return posts;
    }


}
