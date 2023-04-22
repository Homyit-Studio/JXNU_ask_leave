package cn.homyit.onlineLeaveSystem.service.impl;

import org.springframework.mail.SimpleMailMessage;
import cn.homyit.onlineLeaveSystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author 州牧
 * @description
 * @since 2022-08-30 22:08
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public String resetPasswordByEmail(String email,HttpServletRequest request) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("homyit@126.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("重置密码邮件");
        String pwd= UUID.randomUUID().toString().replaceAll("-", "").substring(0,6);
        simpleMailMessage.setText("你好！你的新密码为："+pwd+"\n请登录系统再次修改密码!");

        javaMailSender.send(simpleMailMessage);
        return pwd;

    }

    //获取ip，提示ip已被我们记录
    private String getClientIp(HttpServletRequest request) {
        // 一般都会有代理转发，真实的ip会放在X-Forwarded-For
        String xff = request.getHeader("X-Forwarded-For");
        if (xff == null) {
            return request.getRemoteAddr();
        } else {
            return xff.contains(",") ? xff.split(",")[0] : xff;
        }
    }
}
