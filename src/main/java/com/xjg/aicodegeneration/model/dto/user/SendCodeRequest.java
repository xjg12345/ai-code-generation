package com.xjg.aicodegeneration.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendCodeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    private String email;
}
