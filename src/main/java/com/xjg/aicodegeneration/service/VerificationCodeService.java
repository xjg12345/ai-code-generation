package com.xjg.aicodegeneration.service;

public interface VerificationCodeService {
    /**
     * 发送验证码到指定邮箱
     *
     * @param email 邮箱地址
     */
    void sendCode(String email);

    /**
     * 校验验证码
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return true 校验通过
     */
    boolean verifyCode(String email, String code);
}
