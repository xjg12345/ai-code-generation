package com.xjg.aicodegeneration.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 邮箱（注册时作为账号）
     */
    private String email;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 验证码
     */
    private String verificationCode;
}
