package com.xjg.aicodegeneration.service;

/**
 * 邮件发送服务
 */
public interface MailService {
    /**
     * 发送验证码邮件
     *
     * @param to   收件人邮箱
     * @param code 验证码
     */
    void sendVerificationCode(String to, String code);
}