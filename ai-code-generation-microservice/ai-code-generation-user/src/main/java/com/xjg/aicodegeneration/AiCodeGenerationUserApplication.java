package com.xjg.aicodegeneration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

@SpringBootApplication
@MapperScan("com.xjg.aicodegeneration.mapper")
@ComponentScan("com.xjg")
@EnableDubbo
public class AiCodeGenerationUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeGenerationUserApplication.class, args);
    }
}

