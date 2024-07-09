package cn.fbi.service;


import cn.fbi.common.Result;
import cn.fbi.entity.ChangePassword;
import cn.fbi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
/**
 *
 * 用户相关方法的接口类
 * */
public interface UserService extends IService<User> {

    List<User> getUser() ;
    /** 用户注册 */
    Result Register(User user);
    /** 用户通过账号密码进行登录 */
    Result loginByAccount(User user);
    /** 前端通过token获取用户数据 */
    ResponseEntity<?> getUserProfile(String token);
    /** 用户通过邮箱验证码进行登录 */
    Result loginByEmail(User user);
    /** 用户退出登录 */
    Result logOut();
    /** 通过账号密码验证修改密码 */
    Result changePwdByAccount(ChangePassword changePassword);
    /** 通过邮箱验证码验证修改密码 */
    Result changePwdByMail(ChangePassword changePassword);
}
