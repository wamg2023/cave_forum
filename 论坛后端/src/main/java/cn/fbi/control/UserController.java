package cn.fbi.control;

import cn.fbi.common.Result;
import cn.fbi.entity.ChangePassword;
import cn.fbi.entity.RedisUser;
import cn.fbi.entity.User;
import cn.fbi.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * 用户控制器，处理用户相关的HTTP请求
 * */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getString")
    public String getString(){
        System.out.println("启动springboot");
        return "启动springboot";
    }

    @Autowired
    private UserMapper userMapper;
    @GetMapping("/getUser")
    public List<User> getUser(){
        return userMapper.selectList(null);
    }

    @PostMapping ("/register")
    public Result register(@RequestBody User user){
        String account=user.getUsername();
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("username",account);
        User tempUser=userMapper.selectOne(qw);
        if(tempUser==null){
            userMapper.insert(user);
            return Result.Success("注册成功！");
        }
        else
           return Result.Error("注册失败,用户名已存在！");
    }

    //登录功能
    @PostMapping("/login")
    public Result login(@RequestBody User user){

        String username = user.getUsername();
        String password = user.getPassword();
        User tempUser=userMapper.selectUserID(username,password);
        if(tempUser!=null){
//            Cookie cookie = new Cookie("name", username);
//            Cookie cookie2=new Cookie("pwd",user.getPassword());
//            cookie.setMaxAge(7 * 24 * 60 * 60); // 7天过期
//            cookie.setHttpOnly(true);// 不能被js访问的Cookie
//            response.addCookie(cookie);
//            response.addCookie(cookie2);
            redisTemplate.opsForValue().set("user",user);
            return Result.Success("登录成功！");
        }
        else
            return Result.Error("用户名或密码错误!");
    }

    //注销功能,删除表
    @DeleteMapping("/logout")
    public Result logout(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        User tempUser=userMapper.selectUserID(username,password);
        if(tempUser==null)
            return Result.Error("注销失败，用户名或密码错误");
        else{
            String account=user.getUsername();
            QueryWrapper<User> qw=new QueryWrapper<>();
            qw.eq("username",account);
            userMapper.delete(qw);
            return Result.Success("用户注销成功！");
        }
    }

    //修改表
    @PostMapping("/ChangePw")
    public Result ChangePw(@RequestBody ChangePassword changePassword){
        //读取数据
        String Username =changePassword.getUsername();
        String OldPassWord=changePassword.getOld_password();
        String NewPassword=changePassword.getNew_password();
        String AgainNewPassWord=changePassword.getAgain_new_password();
        //判断前后两次新密码输入是否一致
        if(NewPassword.equals(AgainNewPassWord)){
            //判断用户名和旧密码是否对应，即确报用户本人修改
            User tempUser=userMapper.selectUserID(Username,OldPassWord);
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
            }

        }else{
            return Result.Error("两次新密码输入不一致，请重新输入");
        }
    }


    /**
     * 7.3号更新
     * */
  /*  @GetMapping("/cookie")
    public String readCookie(@CookieValue(value = "name",defaultValue = "默认值") String name){
        return name;
    }
*/
    /*@GetMapping("/allCookie")
    public String readAllCookies(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        if(cookies != null){
            return Arrays.stream(cookies).map(cookie -> cookie.getName() + "=" +cookie.getValue()).collect(Collectors.joining(","));
        }
        return "没有---cookie";
    }*/

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis")
    public void getRedis() {
        redisTemplate.opsForValue().set("name","卷心");
        String name = (String) redisTemplate.opsForValue().get("name");
        //System.out.println(name); //卷心菜
    }

    @PostMapping("/setRedis")
    public RedisUser setUser(@RequestBody RedisUser user){
        redisTemplate.opsForValue().set("user",user);
        return (RedisUser)redisTemplate.opsForValue().get("user");
    }


    /**
     * 7.
     * 分页查询
     */
    @GetMapping("/pages")
    public Result pages(@PathVariable int current, @PathVariable int pagesize) {
        IPage<User> page = new Page<>(current, pagesize);
        userMapper.selectPage(page, null);
        return Result.Success("查询成功",  userMapper.selectPage(page, null));
    }

    //@GetMapping("{id}"):使用浏览器地址访问传递参数的方法请求
    @GetMapping("{id}")//@PathVariable:匹配地址请求中的id
    public Result getById(@PathVariable int id) throws IOException {
        //触发异常拦截
        //if(false) throw new IOException();
        //执行查询
        User user = userMapper.selectById(id);
        //判断是否查询成功
        if (user!=null){
            return Result.Success("查询成功",user);
        }else {
            return Result.Error("查询失败！，没有这个Id为"+id+"对应的数据");
        }
    }

    @GetMapping
    public Result getlist() throws IOException{
        if (userMapper.selectList(null)!=null)throw new IOException();
        return Result.Success("执行查询完成！",userMapper.selectList(null));
    }

    //4.分页查询-模糊查询
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(@PathVariable int currentPage,
                           @PathVariable int pageSize,User user)throws IOException{
        if (false)throw new IOException();
        IPage<User> page = new Page<User>(currentPage,pageSize);
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
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody User user){
        String account=user.getUsername();
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("username",account);
        User tempUser=userMapper.selectOne(qw);
        if(tempUser==null){
            userMapper.insert(user);
            return Result.Success("用户添加成功！");
        }
        else
            return Result.Error("添加失败,用户名已存在！");
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        String Username=user.getUsername();
        UpdateWrapper<User> uW = new UpdateWrapper<>();
        uW.eq("username", Username);
        if (userMapper.update(user,uW)>0){
            return Result.Success("修改完成！");
        }else {
            return Result.Error("修改失败！");
        }

    }

    @DeleteMapping("{id}")
    public Result delById(@PathVariable int id)throws IOException{
        //if (false)throw new IOException();
        if (id>0){
            if (userMapper.deleteById(id)>0){
                return Result.Success("删除成功！");
            }else {
                return Result.Error("请重新执行删除！");
            }
        }else {
            return Result.Error("请重新执行删除！");
        }
    }




}
