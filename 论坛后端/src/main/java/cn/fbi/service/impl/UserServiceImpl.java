package cn.fbi.service.impl;
import cn.fbi.common.LoginResult;
import cn.fbi.common.Result;
import cn.fbi.control.UserController;
import cn.fbi.entity.ChangePassword;
import cn.fbi.entity.EmailVerifyCode;
import cn.fbi.entity.LoginUserToken;
import cn.fbi.entity.User;
import cn.fbi.mapper.UserMapper;
import cn.fbi.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public   List<User> getUser(){
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

    /** 生成基于用户账号和当前时间戳的Token */
    public static String generateToken(String account) {
        //获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timestamp = currentTime.format(formatter);
        //拼接token
        String token = account + "caveforum" + timestamp;
        return token;
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
                //生成日志
                logger.info("注册失败，该账号已存在");
                // 如果账号重复，返回账号重复的错误信息
                return Result.Error("注册失败，账号已存在");

            }else if(existingEmailUser != null){
                //生成日志
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
                    //生成日志
                    logger.info("注册成功，用户ID为" + userID);
                    // 返回注册成功的结果
                    return Result.Success("注册成功");
            }
    }

    /** 前端通过token获取用户数据方法实现 */
    @Override
    public ResponseEntity<?> getUserProfile(String token) {
        LoginUserToken loginUserToken = (LoginUserToken) redisTemplate.opsForValue().get("loginUser");
        String account = loginUserToken.getAccount();
        String redisToken = loginUserToken.getToken();
        //前端传来的token不为空才能进行后续判断，否则，返回报错信息，token已过期
        if(token.length()>0){
            //前端传来的token与redis中存储的token一致，返回用户信息，反之，则返回报错信息，无效Token
            if(token.equals(redisToken)){
                //定义登录查询函数,从缓存表中获取索引
                QueryWrapper<User> qw = new QueryWrapper<>();
                qw.eq("account",account);
                User loginUser=userMapper.selectOne(qw);
                return ResponseEntity.ok(loginUser);
            }
            else{
                //生成日志
                logger.info("无效Token，请求无效！");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效Token");
            }

        }
        else{
            //生成日志
            logger.info("Token已过期，请求无效！");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token已过期");
        }

    }

    /** 账密登录方法实现 */
    public Result loginByAccount(User user){
        //缓存用户账号与token的数据类型
        LoginUserToken loginUserToken = new LoginUserToken();
        //封装返回登录信息结果的数据类型
        LoginResult loginResult = new LoginResult();
        String account = user.getAccount();
        String password = user.getPassword();
        QueryWrapper<User> qw = new QueryWrapper<>();
        //定义登录查询函数
        qw.eq("account",account).eq("password",password);
        User loginUser=userMapper.selectOne(qw);
        if(loginUser!=null){
            //登录成功，生成token并存入Redis
            String token = this.generateToken(account);
            loginUserToken.setAccount(account);
            loginUserToken.setToken(token);
            loginUserToken.setEmail(loginUser.getEmail());
            redisTemplate.opsForValue().set("loginUser", loginUserToken, Duration.ofDays(7));
            //生成日志
            logger.info("用户登录成功！！！当前登录用户的token是:  "+token );
            loginResult.setUser(loginUser);
            loginResult.setToken(token);
            return Result.Success("登录成功！",loginResult);
        }
        else{
            //生成日志
            logger.info("用户登录失败 ");
            return Result.Error("用户名或密码错误!请重新输入！");
        }
    }

    /** 邮箱验证码登录方法实现 */
    public Result loginByEmail(User user){
        //缓存用户账号与token的数据类型
        LoginUserToken loginUserToken = new LoginUserToken();
        //封装返回登录信息结果的数据类型
        LoginResult loginResult = new LoginResult();
        String email = user.getEmail();
        String verifyCode = user.getVerify_code();
        EmailVerifyCode email_verifyCode = (EmailVerifyCode) redisTemplate.opsForValue().get("email_verifyCode") ;
        //前端传来的验证码与redis中存储的验证码一致，返回用户信息，反之，则返回报错信息，无效Token
        if(verifyCode.equals(email_verifyCode.getVerify_code()) && email.equals(email_verifyCode.getEmail())){
            //登录成功，返回用户信息
            //定义登录查询函数,从缓存表中获取索引
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("email",email);
            User loginUser = userMapper.selectOne(qw);
            //生成token并之存入Redis
            //构建存储对象
            String account = loginUser.getAccount();
            String token = this.generateToken(account);
            loginUserToken.setAccount(account);
            loginUserToken.setToken(token);
            loginUserToken.setEmail(loginUser.getEmail());
            redisTemplate.opsForValue().set("loginUser", loginUserToken, Duration.ofDays(7));
            //生成日志
            logger.info("用户登录成功！！！当前登录用户的token是:  "+token );
            loginResult.setUser(loginUser);
            loginResult.setToken(token);
            return Result.Success("登录成功！",loginResult);
        }
        else {
            //生成日志
            logger.info("用户登录失败!!验证码不正确" );
            return Result.Error("验证码错误，请重新输入！");
        }
    }

    /** 用户退出登录方法实现 */
    public Result logOut(){
        //将redis缓存表中的数据清空
        redisTemplate.delete("loginUser");
        logger.info("用户退出登录成功！ ");
        return Result.Success("用户退出登录成功！");
    }

    /** 通过账号密码验证修改密码方法实现 */
    public Result changePwdByAccount(ChangePassword changePassword){
        String account =changePassword.getAccount();
        String password=changePassword.getOldPassword();
        String NewPassword=changePassword.getNewPassword();
        //判断用户名和旧密码是否对应，即确报用户本人修改
        QueryWrapper<User> qw = new QueryWrapper<>();
        //定义查询函数,若能找到则说明用户名和密码怕相匹配，否则返回报错，用户名或密码输入错误
        qw.eq("account",account).eq("password",password);
        User tempUser=userMapper.selectOne(qw);
        if (tempUser!=null){
            //修改密码
            UpdateWrapper<User> uW = new UpdateWrapper<>();
            uW.eq("account", account);
            tempUser.setPassword(NewPassword);
            userMapper.update(tempUser,uW);
            //生成日志
            logger.info("密码修改成功,用户：{}     当前的密码是：{}", account, NewPassword);
            return Result.Success("密码修改成功");
        }
        else{
            //生成日志
            logger.info("错误信息！用户名或密码输入错误!!");
            return Result.Error("用户名或密码输入错误");
        }
    }

    /** 通过邮箱验证码修改密码方法实现 */
    public  Result changePwdByMail(ChangePassword changePassword){
        //前端传来的验证码与redis中存储的验证码一致，返回用户信息，反之，则返回报错信息，无效Token
        String email = changePassword.getEmail();
        String verifyCode = changePassword.getCaptcha();
        //判断用户名和旧密码是否对应，即确报用户本人修改
        QueryWrapper<User> qw = new QueryWrapper<>();
        //定义查询函数,若能找到则说明用户名和密码怕相匹配，否则返回报错，用户名或密码输入错误
        qw.eq("email",email);
        User tempUser=userMapper.selectOne(qw);
        if(tempUser != null){
            EmailVerifyCode email_verifyCode = (EmailVerifyCode) redisTemplate.opsForValue().get("email_verifyCode") ;
            if(verifyCode.equals(email_verifyCode.getVerify_code()) && email.equals(email_verifyCode.getEmail())){
                //修改密码
                UpdateWrapper<User> uW = new UpdateWrapper<>();
                uW.eq("email", email);
                String NewPassword = changePassword.getNewPassword();
                tempUser.setPassword(NewPassword);
                userMapper.update(tempUser,uW);
                //生成日志
                logger.info("密码修改成功,用户：{}     当前的密码是：{}", tempUser.getAccount(), NewPassword);
                return Result.Success("密码修改成功");
            }
            else {
                //生成日志
                logger.info("密码失败!!验证码不正确" );
                return Result.Error("验证码错误，请重新输入！");
            }
        }
        else{
            //生成日志
            logger.info("修改密码失败!!该邮箱不存在账号" );
            return Result.Error("该邮箱不存在账号，请检查邮箱输入！");
        }
    }




}
