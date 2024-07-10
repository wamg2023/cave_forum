package cn.fbi.service;


import cn.fbi.common.Result;
import cn.fbi.common.ChangePassword;
import cn.fbi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
    Result getUserProfile(String token);
    /** 用户通过邮箱验证码进行登录 */
    Result loginByEmail(User user);
    /** 用户退出登录 */
    Result logOut();
    /** 通过账号密码验证修改密码 */
    Result changePwdByAccount(ChangePassword changePassword);
    /** 通过邮箱验证码验证修改密码 */
    Result changePwdByMail(ChangePassword changePassword);
    /** 查看某人信息 */
    Result getUserProfileByAccount(User user);
    /** 修改用户信息 */
    Result changeUserProfile(User user);
    /** 获取关注的人的信息 */
    Result getFollowUser();
}
