package com.xjg.aicodegeneration.service.impl;

import com.xjg.aicodegeneration.exception.BusinessException;
import com.xjg.aicodegeneration.exception.ErrorCode;
import com.xjg.aicodegeneration.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮件发送服务
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    /**
     * 发送验证码邮件
     *
     * @param to   收件人邮箱
     * @param code 验证码
     */
    @Override
    public void sendVerificationCode(String to, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("AI 应用生成平台 - 注册验证码");
            message.setContent(buildHtmlContent(code), "text/html;charset=UTF-8");

            Transport.send(message);
            log.info("验证码邮件发送成功，收件人: {}", to);
        } catch (MessagingException e) {
            log.error("验证码邮件发送失败，收件人: {}", to, e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮件发送失败，请稍后重试");
        }
    }

    private String buildHtmlContent(String code) {
        return "<div style='max-width:600px;margin:0 auto;padding:30px;font-family:Arial,sans-serif;background:#f5f7fa;border-radius:8px;'>"
                + "<h2 style='color:#333;text-align:center;'>AI 应用生成平台</h2>"
                + "<p style='color:#666;text-align:center;font-size:16px;'>您的注册验证码为：</p>"
                + "<div style='text-align:center;margin:30px 0;'>"
                + "<span style='display:inline-block;padding:12px 40px;background:#4e6ef2;color:#fff;font-size:28px;font-weight:bold;letter-spacing:8px;border-radius:6px;'>"
                + code + "</span></div>"
                + "<p style='color:#999;text-align:center;font-size:13px;'>验证码 5 分钟内有效，请勿泄露给他人</p>"
                + "</div>";
    }
}
