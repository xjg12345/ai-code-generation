package com.xjg.aicodegeneration.core.builder;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class VueProjectBuilder {

    @Data
    @AllArgsConstructor
    public static class BuildResult {
        private boolean success;
        private String stage;
        private String message;
        private String commandOutput;
    }

    @Data
    @AllArgsConstructor
    private static class CommandResult {
        private boolean success;
        private String command;
        private String output;
        private String errorMessage;
    }

    /**
     * 异步构建项目（不阻塞主流程）
     *
     * @param projectPath 项目路径
     */
    public void buildProjectAsync(String projectPath) {
        // 在单独的线程中执行构建，避免阻塞主流程
        Thread.ofVirtual().name("vue-builder-" + System.currentTimeMillis()).start(() -> {
            try {
                buildProject(projectPath);
            } catch (Exception e) {
                log.error("异步构建 Vue 项目时发生异常: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * 构建 Vue 项目
     *
     * @param projectPath 项目根目录路径
     * @return 是否构建成功
     */
    public boolean buildProject(String projectPath) {
        return buildProjectWithResult(projectPath).isSuccess();
    }

    /**
     * 构建 Vue 项目并返回详细结果
     *
     * @param projectPath 项目根目录路径
     * @return 构建结果
     */
    public BuildResult buildProjectWithResult(String projectPath) {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            log.error("项目目录不存在: {}", projectPath);
            return new BuildResult(false, "prepare", "项目目录不存在: " + projectPath, "");
        }
        // 检查 package.json 是否存在
        File packageJson = new File(projectDir, "package.json");
        if (!packageJson.exists()) {
            log.error("package.json 文件不存在: {}", packageJson.getAbsolutePath());
            return new BuildResult(false, "prepare", "package.json 文件不存在: " + packageJson.getAbsolutePath(), "");
        }
        log.info("开始构建 Vue 项目: {}", projectPath);
        // 执行 npm install
        CommandResult installResult = executeNpmInstall(projectDir);
        if (!installResult.isSuccess()) {
            log.error("npm install 执行失败");
            return new BuildResult(false, "npm_install", installResult.getErrorMessage(), installResult.getOutput());
        }
        // 执行 npm run build
        CommandResult buildResult = executeNpmBuild(projectDir);
        if (!buildResult.isSuccess()) {
            log.error("npm run build 执行失败");
            return new BuildResult(false, "npm_build", buildResult.getErrorMessage(), buildResult.getOutput());
        }
        // 验证 dist 目录是否生成
        File distDir = new File(projectDir, "dist");
        if (!distDir.exists()) {
            log.error("构建完成但 dist 目录未生成: {}", distDir.getAbsolutePath());
            return new BuildResult(false, "verify_dist", "构建完成但 dist 目录未生成: " + distDir.getAbsolutePath(), buildResult.getOutput());
        }
        log.info("Vue 项目构建成功，dist 目录: {}", distDir.getAbsolutePath());
        return new BuildResult(true, "success", "Vue 项目构建成功", buildResult.getOutput());
    }


    /**
     * 执行命令
     *
     * @param workingDir     工作目录
     * @param command        命令字符串
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否执行成功
     */
    private CommandResult executeCommand(File workingDir, String command, int timeoutSeconds) {
        try {
            log.info("在目录 {} 中执行命令: {}", workingDir.getAbsolutePath(), command);
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            processBuilder.directory(workingDir);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            CompletableFuture<String> outputFuture = readStreamAsync(process.getInputStream());
            // 等待进程完成，设置超时
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            String output = getCommandOutput(outputFuture);
            if (!finished) {
                log.error("命令执行超时（{}秒），强制终止进程", timeoutSeconds);
                process.destroyForcibly();
                return new CommandResult(false, command, output,
                        String.format("命令执行超时（%s秒）", timeoutSeconds));
            }
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("命令执行成功: {}", command);
                return new CommandResult(true, command, output, "");
            } else {
                log.error("命令执行失败，退出码: {}", exitCode);
                return new CommandResult(false, command, output,
                        String.format("命令执行失败，退出码: %s", exitCode));
            }
        } catch (Exception e) {
            log.error("执行命令失败: {}, 错误信息: {}", command, e.getMessage());
            return new CommandResult(false, command, "", "执行命令失败: " + e.getMessage());
        }
    }

    /**
     * 执行 npm install 命令
     */
    private CommandResult executeNpmInstall(File projectDir) {
        log.info("执行 npm install...");
        String command = String.format("%s install", buildCommand("npm"));
        return executeCommand(projectDir, command, 300); // 5分钟超时
    }

    /**
     * 执行 npm run build 命令
     */
    private CommandResult executeNpmBuild(File projectDir) {
        log.info("执行 npm run build...");
        String command = String.format("%s run build", buildCommand("npm"));
        return executeCommand(projectDir, command, 180); // 3分钟超时
    }


    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private String buildCommand(String baseCommand) {
        if (isWindows()) {
            return baseCommand + ".cmd";
        }
        return baseCommand;
    }

    private CompletableFuture<String> readStreamAsync(InputStream inputStream) {
        return CompletableFuture.supplyAsync(() -> IoUtil.read(inputStream, StandardCharsets.UTF_8));
    }

    private String getCommandOutput(CompletableFuture<String> outputFuture) {
        try {
            return outputFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("读取命令输出失败: {}", e.getMessage());
            return "";
        }
    }

}
