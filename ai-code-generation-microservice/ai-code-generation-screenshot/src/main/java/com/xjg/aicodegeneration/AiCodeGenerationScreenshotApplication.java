package com.xjg.aicodegeneration;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class AiCodeGenerationScreenshotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeGenerationScreenshotApplication.class, args);
    }
}

