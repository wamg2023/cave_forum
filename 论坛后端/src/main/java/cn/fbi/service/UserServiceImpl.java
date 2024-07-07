package cn.fbi.service;
import cn.fbi.entity.User;
import cn.fbi.mapper.UserMapper;
import cn.fbi.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 *
 * 接口实现类
 * */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    public  List<User> getUser(){
        return userMapper.selectList(null);
    }
}
