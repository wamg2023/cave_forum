package cn.fbi.control;

import cn.fbi.common.Result;
import cn.fbi.entity.ChangePassword;
import cn.fbi.entity.User;
import cn.fbi.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 *
 * 用户控制器，处理用户相关的HTTP请求
 * */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    /** 用户注册请求 */
    @PostMapping ("/register")
    public Result register(@RequestBody User user){
        return userService.Register(user);
    }
    /** 获取用户个人资料请求 */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(token);
    }

    /** 用户登录请求，通过账号密码进行登录 */
    @PostMapping("/loginByAccount")
    public Result loginByAccount(@RequestBody User user){
        return userService.loginByAccount(user);
    }

    /** 用户登录请求，通过邮箱验证码进行登录 */
    @PostMapping("/loginByEmail")
    public Result loginByEmail(@RequestBody User user){
        return userService.loginByEmail(user);
    }

    /** 用户退出登录请求 */
    @DeleteMapping("/logout")
    public Result logout(){
        return userService.logOut();
    }

    /** 通过账号密码验证修改密码请求 */
    @PostMapping("/changePwdByAccount")
    public Result changePwdByAccount(@RequestBody ChangePassword changePassword){
        return userService.changePwdByAccount(changePassword);
    }

    /** 通过邮箱验证码修改密码请求 */
    @PostMapping("/changePwdByEmail")
    public Result ChangePwdByMail(@RequestBody ChangePassword changePassword){
        return userService.changePwdByMail(changePassword);
      /*  String storedVerifyCode = (String) redisTemplate.opsForValue().get("verifyCode");
        if (storedVerifyCode == null) {
            return Result.Error("邮箱验证码已过期，请重新获取验证码！");
        }
        String userVerifyCode = changePassword.getVerifyCode();
        if (!storedVerifyCode.equals(userVerifyCode)) {
            return Result.Error("邮箱验证码不正确，请重新输入验证码");
        }
        //读取数据
        String Username =changePassword.getUsername();
        String OldPassWord=changePassword.getOld_password();
        String NewPassword=changePassword.getNew_password();
        String AgainNewPassWord=changePassword.getAgain_new_password();
        //判断用户名和旧密码是否对应，即确报用户本人修改


        if (tempUser!=null){
            //修改密码
            UpdateWrapper<User> uW = new UpdateWrapper<>();
            uW.eq("username", Username);
            User user = new User();
            user.setPassword(NewPassword);
            userMapper.update(user,uW);
            return Result.Success("密码修改成功");
        }
        else{
            return Result.Error("用户名或密码输入错误");
        }*/
    }





    /*@GetMapping("/redis")
    public void getRedis() {
        redisTemplate.opsForValue().set("name","卷心");
        String name = (String) redisTemplate.opsForValue().get("name");
        //System.out.println(name); //卷心菜
    }

    @PostMapping("/setRedis")
    public RedisUser setUser(@RequestBody RedisUser user){
        redisTemplate.opsForValue().set("user",user, Duration.ofMinutes(5));
        return (RedisUser)redisTemplate.opsForValue().get("user");
    }
*/

    /**
     * 7.
     * 分页查询
     */
    @GetMapping("/pages")
    public Result pages(@PathVariable int current, @PathVariable int pagesize) {
        /*IPage<User> page = new Page<>(current, pagesize);
        userMapper.selectPage(page, null);
        return Result.Success("查询成功",  userMapper.selectPage(page, null));*/
        return Result.Success("ss");
    }

    //@GetMapping("{id}"):使用浏览器地址访问传递参数的方法请求
    @GetMapping("{id}")//@PathVariable:匹配地址请求中的id
    public Result getById(@PathVariable int id)  {
        /*//执行查询
        User user = userMapper.selectById(id);
        //判断是否查询成功
        if (user!=null){
            return Result.Success("查询成功",user);
        }else {
            return Result.Error("查询失败！，没有这个Id为"+id+"对应的数据");
        }*/
        return Result.Success("ss");
    }

    @GetMapping
    public Result getlist() throws IOException{
       /* if (userMapper.selectList(null)!=null)throw new IOException();
        return Result.Success("执行查询完成！",userMapper.selectList(null));*/
        return Result.Success("ss");
    }

    //4.分页查询-模糊查询
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(@PathVariable int currentPage, @PathVariable int pageSize,User user){
        /*IPage<User> page = new Page<User>(currentPage,pageSize);
        QueryWrapper qw = new QueryWrapper();
        if (user.getUsername()!=""||user.getAge()!=null||user.getEmail()!=""){
            if (user.getUsername()!=""){
                qw.like("username",user.getUsername());
            }
            if (user.getAge()!=null){
                qw.like("age",user.getAge());
            }
            if (user.getEmail()!=""){
                qw.like("email",user.getEmail());
            }
            page = userMapper.selectPage(page,qw);
        }else {
            page = userMapper.selectPage(page,null);
        }
        //判断请求查询的页码值是否大于总页码值
        // 总页码值
        float count = page.getTotal()/page.getSize();
        if (count>1) {
            if (currentPage<=count){
                return Result.Success("查询完成！",page);
            }else {
                return Result.Success("请重新查询");
            }
        }else {
            return Result.Success("查询完成！",page);
        }*/
        return Result.Success("ss");
    }

    @PostMapping("/add")
    public Result add(@RequestBody User user){
       /* String account=user.getUsername();
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("username",account);
        User tempUser=userMapper.selectOne(qw);
        if(tempUser==null){
            userMapper.insert(user);
            return Result.Success("用户添加成功！");
        }
        else
            return Result.Error("添加失败,用户名已存在！");*/
        return Result.Success("ss");
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        /*String Username=user.getUsername();
        UpdateWrapper<User> uW = new UpdateWrapper<>();
        uW.eq("username", Username);
        if (userMapper.update(user,uW)>0){
            return Result.Success("修改完成！");
        }else {
            return Result.Error("修改失败！");
        }*/
        return Result.Success("ss");

    }

    @DeleteMapping("{id}")
    public Result delById(@PathVariable int id){
        /*if (id>0){
            if (userMapper.deleteById(id)>0){
                return Result.Success("删除成功！");
            }else {
                return Result.Error("请重新执行删除！");
            }
        }else {
            return Result.Error("请重新执行删除！");
        }*/
        return Result.Success("ss");
    }




}
