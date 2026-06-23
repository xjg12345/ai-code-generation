package com.xjg.aicodegeneration.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xjg.aicodegeneration.model.dto.app.AppAddRequest;
import com.xjg.aicodegeneration.model.dto.app.AppQueryRequest;
import com.xjg.aicodegeneration.model.entity.App;
import com.xjg.aicodegeneration.model.entity.User;
import com.xjg.aicodegeneration.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/xjg12345">程序员xjg</a>
 */
public interface AppService extends IService<App> {
    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 门面生成代码
     * @param appId
     * @param message
     * @param loginUser
     * @return
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     * @param appId
     * @param loginUser
     * @return
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 删除应用时关联删除对话历史
     *
     * @param id 应用ID
     * @return 是否成功
     */
    boolean removeById(Serializable id);

    /**
     * 异步生成应用截图并更新封面
     *
     * @param appId  应用ID
     * @param appUrl 应用访问URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 创建应用, 并根据 initPrompt 选择代码生成类型(智能路由）
     * @param appAddRequest
     * @param loginUser
     * @return
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);
}
