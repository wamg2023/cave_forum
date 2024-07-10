package cn.fbi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class ThreadService {

    @Autowired
    private EmailService emailService;

    /**
     * 发送登陆的邮箱验证码，异步执行
     * @param to 收件人
     */
    @Async("taskExecutor")
    public void sendVerifyCode(String to) {
        emailService.sendVerifyCode(to);
    }

    /**
     * 发送修改密码的邮箱验证码，异步执行
     * @param email 收件人邮箱
     */
    @Async("taskExecutor")
    public void sendChangePwdVerifyCode(String email) {
        emailService.sendChangePwdVerifyCode(email);
    }
}
