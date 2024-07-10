package cn.fbi.control;
import cn.fbi.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 *
 * Author:王顺康
 *评论控制器，处理邮箱验证码相关的HTTP请求
 * */
@RestController
@RequestMapping("/email")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private ThreadService emailService;
    @PostMapping("/sendLoginYzm")
    public void sendVerificationCode(@RequestBody Map<String, String> data) {
        String email=data.get("email");
        emailService.sendVerifyCode(email);
    }

    @PostMapping("/sendChangePwdYzm")
    public void sendChangePwdVerificationCode(@RequestBody Map<String, String> data) {
        String email=data.get("email");
        emailService.sendChangePwdVerifyCode(email);
    }

}
