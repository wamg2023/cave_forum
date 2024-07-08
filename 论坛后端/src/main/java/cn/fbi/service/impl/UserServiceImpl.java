package cn.fbi.service.impl;
import cn.fbi.common.Result;
import cn.fbi.control.UserController;
import cn.fbi.entity.User;
import cn.fbi.mapper.UserMapper;
import cn.fbi.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;


/**
 *
 * 接口实现类
 * */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserMapper userMapper;

    public  List<User> getUser(){
        return userMapper.selectList(null);
    }

    /** 生成n位用户ID */
    private String generateUniqueUserId(int length) {
        String userId;
        do {
            userId = generateRandomUserId(length);
        } while (userIdExists(userId));
        return userId;
    }

    /** 生成随机的n位数字作为ID */
    private String generateRandomUserId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0-9的随机数字
        }
        return sb.toString();
    }

    /** 检查生成的userId是否已存在 */
    private boolean userIdExists(String userId) {
        QueryWrapper<User> qw =new QueryWrapper<>();
        qw.eq("user_id", userId);
        return userMapper.selectCount(qw) > 0;
    }

    /** 注册方法实现 */
    public Result Register(User user){
            String account = user.getAccount();
            String email = user.getEmail();
            // 查询账号是否重复
            QueryWrapper<User> accountQw = new QueryWrapper<>();
            accountQw.eq("account", account);
            User existingAccountUser = userMapper.selectOne(accountQw);
            // 查询邮箱是否重复
            QueryWrapper<User> emailQw = new QueryWrapper<>();
            emailQw.eq("email", email);
            User existingEmailUser = userMapper.selectOne(emailQw);
            // 如果账号和邮箱都不重复，生成6位不重复的ID并插入用户表
            if (existingAccountUser != null){
                logger.info("注册失败，该账号已存在");
                // 如果账号重复，返回账号重复的错误信息
                return Result.Error("注册失败，账号已存在");

            }else if(existingEmailUser != null){
                logger.info("注册失败，该邮箱已存在");
                // 如果邮箱重复，返回邮箱重复的错误信息
                return Result.Error( "该邮箱已被使用");
            }else{
                    // 生成6位不重复的ID
                    String userID = this.generateUniqueUserId(6);
                    //生成用户头像存储路径
                    String avatar = userID + "Avatar.png";
                    int userId = Integer.parseInt(userID);
                    user.setUser_id(userId);
                    user.setAvatar(avatar);
                    //插入新用户
                    userMapper.insert(user);
                    logger.info("注册成功，用户ID为" + userID);
                    // 返回注册成功的结果
                    return Result.Success("注册成功");
            }
    }

    

}
