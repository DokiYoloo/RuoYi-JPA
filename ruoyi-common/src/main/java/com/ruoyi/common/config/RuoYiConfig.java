package com.ruoyi.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ruoyi")
public class RuoYiConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    private static String captchaType;

    /**
     * TODO lookup affected surface
     */
    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    /**
     * TODO lookup affected surface
     */
    public static String getProfile() {
        return profile;
    }

    /**
     * TODO lookup affected surface
     */
    public static String getCaptchaType() {
        return captchaType;
    }

    /**
     * resolve path.
     */
    public static String resolvePath(String path) {
        return getProfile() + path;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return resolvePath("/import");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return resolvePath("/avatar");
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return resolvePath("/download/");
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return resolvePath("/upload");
    }
}
