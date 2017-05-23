package xin.fallen.config;

import xin.fallen.annotation.Alias;

/**
 * Author: Fallen
 * Date: 2017/3/29
 * Time: 10:00
 * Usage:
 */
public class StaticConfig {
//==========================外网==========================

    @Alias("wan_dir_path")
    public static String WanDirPath;

    @Alias("wan_path_replacement")
    public static String WanPathReplacement;

    @Alias("wan_url_replacement")
    public static String WanUrlReplacement;

    @Alias("base64_encode_prefix")
    public static String Base64EncodePrefix;

    @Alias("remote_notify_url")
    public static String RemoteNotifyUrl;
}