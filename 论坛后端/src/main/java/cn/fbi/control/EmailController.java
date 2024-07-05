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
        String text = "你的验证码是: " + verificationCode;
        emailService.sendEmail(email, subject, text);
    }
    private String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
