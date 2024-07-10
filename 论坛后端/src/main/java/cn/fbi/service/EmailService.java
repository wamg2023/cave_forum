package cn.fbi.service;

import cn.fbi.control.EmailController;
import cn.fbi.common.EmailVerifyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Random;


/**
 *
 * 发送邮箱验证码的服务类
 * */
@Component
@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    private String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000));
    }
    /** 发送邮箱验证码
     * @Parm Sring to ,要发送的邮箱地址
     * */
    public void sendVerifyCode(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        String verificationCode = this.generateVerificationCode();
        //将邮箱及验证码存入redis中，并设置有效时间为5分钟
        EmailVerifyCode emailVerifyCode = new EmailVerifyCode();
        emailVerifyCode.setEmail(to);
        emailVerifyCode.setVerify_code(verificationCode);
        redisTemplate.opsForValue().set("email_verifyCode",emailVerifyCode, Duration.ofMinutes(5));
        String subject = "Genshin Impact";
        String text = "亲爱的用户：\n" +
                "欢迎使用原神游戏论坛，您本次操作的验证码为：\n\n" +
                verificationCode + "\n\n" +
                "此验证码5分钟内有效，请立即进行下一步操作。 如非你本人操作，请忽略此邮件。\n" +
                "感谢您的使用！";
        message.setFrom("15294798563@163.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        logger.info("验证码发送成功！"+"  本次验证码为： "+verificationCode+"  有效时间：5min");
    }
}
