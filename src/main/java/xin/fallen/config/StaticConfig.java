package xin.fallen.config;

import xin.fallen.annotation.Alias;

public class StaticConfig {
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
    @Alias("wan_dir_path_pre")
    public static String WanDirPathPre;
    @Alias("wan_path_replacement_pre")
    public static String WanPathReplacementPre;
    @Alias("wan_url_replacement_pre")
    public static String WanUrlReplacementPre;
    @Alias("remote_notify_url_pre")
    public static String RemoteNotifyUrlPre;
}
