package com.xjg.aicodegeneration.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ProjectDownloadService {

    /**
     * 下载项目作为压缩包
     * @param projectPath 项目路径
     * @param downloadFileName 下载文件名
     * @param response
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
