package com.xjg.aicodegeneration.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xjg.aicodegeneration.exception.BusinessException;
import com.xjg.aicodegeneration.exception.ErrorCode;
import com.xjg.aicodegeneration.service.MailService;
import com.xjg.aicodegeneration.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 */
@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final String CODE_PREFIX = "verification_code:";
    private static final long CODE_EXPIRE_MINUTES = 5;
    private static final long SEND_INTERVAL_SECONDS = 60;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MailService mailService;

    /**
     * 发送验证码到指定邮箱
     *
     * @param email 邮箱地址
     */
    @Override
    public void sendCode(String email) {
        // 检查发送间隔
        String intervalKey = "verification_interval:" + email;
        String lastSendTime = stringRedisTemplate.opsForValue().get(intervalKey);
        if (lastSendTime != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "发送过于频繁，请 " + SEND_INTERVAL_SECONDS + " 秒后再试");
        }

        // 生成 6 位数字验证码
        String code = RandomUtil.randomNumbers(6);

        // 发送邮件
        mailService.sendVerificationCode(email, code);

        // 存储验证码到 Redis，有效期 5 分钟
        String codeKey = CODE_PREFIX + email;
        stringRedisTemplate.opsForValue().set(codeKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 记录发送时间，防止频繁发送
        stringRedisTemplate.opsForValue().set(intervalKey, String.valueOf(System.currentTimeMillis()),
                SEND_INTERVAL_SECONDS, TimeUnit.SECONDS);

        log.info("验证码已发送到邮箱: {}", email);
    }

    /**
     * 校验验证码
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return true 校验通过
     */
    @Override
    public boolean verifyCode(String email, String code) {
        String codeKey = CODE_PREFIX + email;
        String storedCode = stringRedisTemplate.opsForValue().get(codeKey);
        if (storedCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期，请重新获取");
        }
        if (!storedCode.equals(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误，请检查后重新输入");
        }
        // 验证成功后删除验证码
        stringRedisTemplate.delete(codeKey);
        return true;
    }
}

