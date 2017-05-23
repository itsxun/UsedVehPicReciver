package xin.fallen.util;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;

/**
 * Author: Fallen
 * Date: 2017/3/29
 * Time: 16:48
 * Usage:
 */
public class Base64Util {
    public static String encode(File file) {
        byte[] data;
        String res = "";
        Base64 base = new Base64();
        try {
            data = FileUtils.readFileToByteArray(file);
            res = base.encodeToString(data);
        } catch (Exception e) {
            System.out.println("解码出错");
        }
        return res == null ? "" : res;
    }

    public static byte[] decode(String pre) {
        Base64 base = new Base64();
        return base.decode(pre);
    }
}