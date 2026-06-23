package com.xjg.aicodegeneration.service;

public interface ScreenshotService {
    /**
     * 生成并上传网页截图到对象存储
     *
     * @param webUrl 网页URL
     * @return 对象存储访问URL，失败返回null
     */
    String generateAndUploadScreenshot(String webUrl);
}
