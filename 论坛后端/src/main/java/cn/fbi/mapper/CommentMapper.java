package cn.fbi.mapper;

import cn.fbi.entity.Comment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /** 只看帖主评论 */
    @Select("select comment.* from comment join post on post.post_id = comment.post_id where comment.user_id = post.user_id and comment.post_id = #{post_id}")
    List<Comment> selectComments(int post_id);
    /** 获取评论者的昵称 */
    @Select("select nickname from user where user_id=#{user_id}")
    String getUserNameById(int user_id);

}
