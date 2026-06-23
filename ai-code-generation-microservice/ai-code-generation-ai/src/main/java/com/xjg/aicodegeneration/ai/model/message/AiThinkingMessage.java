package com.xjg.aicodegeneration.ai.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AI 思考过程消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AiThinkingMessage extends StreamMessage {

    private String data;

    public AiThinkingMessage(String data) {
        super(StreamMessageTypeEnum.AI_THINKING.getValue());
        this.data = data;
    }
}
