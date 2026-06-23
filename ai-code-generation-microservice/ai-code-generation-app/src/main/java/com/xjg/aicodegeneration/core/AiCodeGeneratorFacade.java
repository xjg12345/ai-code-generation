package com.xjg.aicodegeneration.core;

import cn.hutool.json.JSONUtil;
import com.xjg.aicodegeneration.ai.AiCodeGeneratorService;
import com.xjg.aicodegeneration.ai.AiCodeGeneratorServiceFactory;
import com.xjg.aicodegeneration.ai.model.HtmlCodeResult;
import com.xjg.aicodegeneration.ai.model.MultiFileCodeResult;
import com.xjg.aicodegeneration.ai.model.message.AiResponseMessage;
import com.xjg.aicodegeneration.ai.model.message.BuildRetryMessage;
import com.xjg.aicodegeneration.ai.model.message.ToolExecutedMessage;
import com.xjg.aicodegeneration.ai.model.message.ToolRequestMessage;
import com.xjg.aicodegeneration.constant.AppConstant;
import com.xjg.aicodegeneration.core.builder.VueProjectBuilder;
import com.xjg.aicodegeneration.core.parser.CodeParserExecutor;
import com.xjg.aicodegeneration.core.saver.CodeFileSaverExecutor;
import com.xjg.aicodegeneration.exception.BusinessException;
import com.xjg.aicodegeneration.exception.ErrorCode;
import com.xjg.aicodegeneration.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {
    private static final int MAX_VUE_BUILD_RETRY_COUNT = 5;

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;
    @Resource
    private VueProjectBuilder vueProjectBuilder;


    /**
     * 统一入口：根据类型生成并保存代码（使用 appId）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 根据 appId 获取对应的 AI 服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);

        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式，使用 appId）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用 ID
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }

        // 根据 appId 获取对应的 AI 服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                yield processVueProjectStream(aiCodeGeneratorService, userMessage, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };

    }

    /**
     * 通用流式代码处理方法（使用 appId）
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @param appId       应用 ID
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            // 流式返回完成后保存代码
            try {
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码
                File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
    }

    /**
     * 处理 Vue 项目的流式生成和自动修复流程
     *
     * @param aiCodeGeneratorService AI 服务
     * @param userMessage            用户消息
     * @param appId                  应用 ID
     * @return Flux<String> 流式响应
     */
    private Flux<String> processVueProjectStream(AiCodeGeneratorService aiCodeGeneratorService, String userMessage, Long appId) {
        return Flux.create(sink -> {
            processVueProjectAttempt(aiCodeGeneratorService, appId, userMessage, sink, 1);
        });
    }

    /**
     * 执行单轮 Vue 项目生成；构建失败时自动把错误反馈给 AI 再试一轮
     */
    private void processVueProjectAttempt(AiCodeGeneratorService aiCodeGeneratorService,
                                          Long appId,
                                          String userMessage,
                                          reactor.core.publisher.FluxSink<String> sink,
                                          int attempt) {
        if (sink.isCancelled()) {
            return;
        }
        TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
        tokenStream.onPartialResponse((String partialResponse) -> {
                    AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                    sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                })
                .beforeToolExecution((BeforeToolExecution beforeToolExecution) -> {
                    ToolRequestMessage toolRequestMessage = new ToolRequestMessage(beforeToolExecution.request());
                    sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                })
                .onToolExecuted((ToolExecution toolExecution) -> {
                    ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                    sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                })
                .onCompleteResponse((ChatResponse response) -> {
                    String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + "vue_project_" + appId;
                    VueProjectBuilder.BuildResult buildResult = vueProjectBuilder.buildProjectWithResult(projectPath);
                    if (buildResult.isSuccess()) {
                        sink.complete();
                        return;
                    }
                    if (attempt >= MAX_VUE_BUILD_RETRY_COUNT) {
                        String errorMessage = String.format("Vue 项目构建失败，已达到最大自动修复次数（%s 次）。最后一次失败阶段：%s，原因：%s",
                                MAX_VUE_BUILD_RETRY_COUNT, buildResult.getStage(), buildResult.getMessage());
                        sink.error(new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage));
                        return;
                    }
                    String retryPrompt = buildVueProjectRetryPrompt(attempt + 1, buildResult);
                    BuildRetryMessage buildRetryMessage = new BuildRetryMessage(
                            retryPrompt,
                            String.format("\n\n[系统] 第 %s 轮构建失败，已将错误反馈给 AI 自动修复，开始第 %s 轮生成...\n\n", attempt, attempt + 1)
                    );
                    sink.next(JSONUtil.toJsonStr(buildRetryMessage));
                    processVueProjectAttempt(aiCodeGeneratorService, appId, retryPrompt, sink, attempt + 1);
                })
                .onError((Throwable error) -> {
                    log.error("Vue 项目流式生成失败，appId: {}, attempt: {}", appId, attempt, error);
                    sink.error(error);
                })
                .start();
    }

    private String buildVueProjectRetryPrompt(int nextAttempt, VueProjectBuilder.BuildResult buildResult) {
        return String.format("""
                刚才生成的 Vue 项目在本地构建失败，无需重新解释，直接继续使用工具修复现有项目文件，然后再次确保项目可以成功构建。

                这是第 %s 轮修复。
                失败阶段：%s
                失败原因：%s

                构建日志如下：
                %s

                要求：
                1. 只修改与当前构建错误相关的文件，优先最小化修改。
                2. 必须直接使用工具读取、修改或重写现有文件，不要输出完整代码块给用户。
                3. 修复后继续以可运行、可构建为目标。
                """, nextAttempt, buildResult.getStage(), buildResult.getMessage(), truncateText(buildResult.getCommandOutput(), 6000));
    }

    private String truncateText(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "\n...(日志已截断)";
    }

}
