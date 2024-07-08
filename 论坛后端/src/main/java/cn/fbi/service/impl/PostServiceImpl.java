package cn.fbi.service.impl;


/**
 *
 * 接口实现类
 * */
import cn.fbi.entity.Post;
import cn.fbi.helper.UniqueIdGenerator;
import cn.fbi.mapper.PostMapper;
import cn.fbi.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> getPost(String searchTerm, String userId, String tagId, String sortBy) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();

        //模糊查询条件
        if (searchTerm != null && !searchTerm.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("title", searchTerm));
        }

        //发布者分类
        if (userId != null && !userId.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("userId", userId));
        }

        //标签条件
        if (tagId !=null && !tagId.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("tagId", tagId));
        }

        //排序条件
        if ("time".equals(sortBy)) {
            queryWrapper.orderByDesc("create_time");
        }else if ("hot".equals(sortBy)) {
            queryWrapper.orderByDesc("likeContent");
        }

        //返回查询结果
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public void postPost(String title, String content, String tag, String coverImage) {
        //生成8位不重复的帖子id
        int postId = generateUniqueId();
        //如果有重复的id则重新生成，知道没有为止
        while (postMapper.existsPostId(postId)){
            postId = generateUniqueId();
        }

        //获取当前时间
        LocalDateTime createTime = LocalDateTime.now();

        //把LocalDateTime转换成Date类型
        Date postTime = Date.valueOf(createTime.toLocalDate());

        //创建一个帖子对象
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle(title);
        post.setContent(content);
        post.setTagId(tag);
        post.setCover(coverImage);
        post.setCreateTime(postTime);

        //把post的数据插入到数据库里面
        postMapper.insert(post);

    }
    private Integer generateUniqueId() {
        //随机生成一个8位不重复的id
        return UniqueIdGenerator.generateUniqueId();
    }
}
