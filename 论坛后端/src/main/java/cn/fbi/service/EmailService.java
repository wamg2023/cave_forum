package cn.fbi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 *
 * 发送邮箱验证码的服务类
 * */
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("15294798563@163.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        //System.out.println(message);
        mailSender.send(message);
    }
}
