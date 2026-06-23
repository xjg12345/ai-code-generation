package com.xjg.aicodegeneration;

import com.xjg.aicodegeneration.ai.AiCodeGeneratorService;
import com.xjg.aicodegeneration.ai.model.HtmlCodeResult;
import com.xjg.aicodegeneration.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个程序员的工作记录小工具");
        Assertions.assertNotNull(result);
        log.info("生成的 HTML 代码: {}", result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个程序员的留言板");
        Assertions.assertNotNull(multiFileCode);
        log.info("生成的多文件代码: {}", multiFileCode);
    }

}
