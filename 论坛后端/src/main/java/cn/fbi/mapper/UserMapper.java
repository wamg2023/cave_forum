package cn.fbi.mapper;

import cn.fbi.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserMapper extends BaseMapper<User> {
    /**  登录验证  **/
    @Select("select * from tb_user where username=#{username} and password=#{password}")
    User selectUserID(String username, String password);
}
