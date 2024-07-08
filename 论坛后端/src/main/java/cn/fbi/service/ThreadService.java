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
     * 发送邮箱
     * @param to 收件人
     */
    @Async("taskExecutor")
    public void sendVerifyCode(String to) {
        emailService.sendVerifyCode(to);
    }
}
