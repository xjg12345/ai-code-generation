package com.xjg.aicodegeneration.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xjg.aicodegeneration.model.dto.chathistory.ChatHistoryQueryRequest;
import com.xjg.aicodegeneration.model.entity.ChatHistory;
import com.xjg.aicodegeneration.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/xjg12345">程序员xjg</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 添加对话历史
     * @param appId
     * @param message
     * @param messageType
     * @param userId
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 删除应用下的所有对话历史
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * 获取查询包装类
     *
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 分页查询应用下的对话历史
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                                      LocalDateTime lastCreateTime,
                                                      User loginUser);

    /**
     * 历史对话加载
     * @param appId
     * @param chatMemory
     * @param maxCount
     * @return
     */
     int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
