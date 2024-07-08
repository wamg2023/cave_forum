package cn.fbi.service;


import cn.fbi.common.Result;
import cn.fbi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
/**
 *
 * 用户相关方法的接口类
 * */
public interface UserService extends IService<User> {

    public List<User> getUser() ;
    /** 用户注册 */
    public Result Register(User user);

}
