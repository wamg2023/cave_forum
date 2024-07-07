package cn.fbi.mapper;

import cn.fbi.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    //自定义方法：检查postID是否存在
    boolean existsPostId(int postId);
}
