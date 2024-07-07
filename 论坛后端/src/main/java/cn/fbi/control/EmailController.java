package cn.fbi.control;

import cn.fbi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/sendYzm")
    public void sendVerificationCode(@RequestBody Map<String, String> data) {
        String email=data.get("email");
        String verificationCode = generateVerificationCode();
        String subject = "验证码";
        String text = "亲爱的用户：\n" +
                "您此次的验证码为：\n\n" +
                verificationCode + "\n\n" +
                "此验证码5分钟内有效，请立即进行下一步操作。 如非你本人操作，请忽略此邮件。\n" +
                "感谢您的使用！";
        emailService.sendEmail(email, subject, text);
    }
    private String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
