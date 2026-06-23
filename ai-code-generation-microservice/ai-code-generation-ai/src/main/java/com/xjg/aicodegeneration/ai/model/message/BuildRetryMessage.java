package com.xjg.aicodegeneration.ai.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Vue 项目构建失败后的自动重试消息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BuildRetryMessage extends StreamMessage {

    private String retryPrompt;

    private String displayMessage;

    public BuildRetryMessage(String retryPrompt, String displayMessage) {
        super(StreamMessageTypeEnum.BUILD_RETRY.getValue());
        this.retryPrompt = retryPrompt;
        this.displayMessage = displayMessage;
    }
}
