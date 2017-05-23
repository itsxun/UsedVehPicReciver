package xin.fallen.controller;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.fallen.config.StaticConfig;
import xin.fallen.domain.Fallen;
import xin.fallen.util.Base64Util;
import xin.fallen.util.HttpUtil;
import xin.fallen.util.JsonResultUtil;
import xin.fallen.vo.Callback;
import xin.fallen.vo.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Author: Fallen
 * Date: 2017/3/22
 * Time: 16:23
 * Usage:史上最垃圾的controller
 */
@RestController
public class PicUploadCtrl {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/new")
    public JsonResult newRecord(HttpServletRequest req, @NotEmpty String jylsh, @NotEmpty String zplx, HttpServletResponse resp) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = req.getReader();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (Exception e) {
            log.error("取出参数错误，原因是：", e.getMessage());
        }
        String pic = sb.toString();
        Gson gson = new Gson();
        Fallen fallen = gson.fromJson(pic, Fallen.class);
        if (fallen == null || fallen.getJylsh() == null || fallen.getZplx() == null || fallen.getPic() == null) {
            return JsonResultUtil.resDispatcher("请提供正确的交易流水号与照片类型", 0);
        }
        Date date = new Date();
        File file = new File(StaticConfig.WanDirPath + File.separator + sdf.format(date) + File.separator + fallen.getJylsh());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = new File(file + File.separator + fallen.getZplx() + "_" + UUID.randomUUID().toString().toUpperCase() + ".JPG");
        FileUtils.writeByteArrayToFile(file, Base64Util.decode(fallen.getPic().replace(StaticConfig.Base64EncodePrefix, "")));
        String res = file.getAbsolutePath().replace(StaticConfig.WanPathReplacement, StaticConfig.WanUrlReplacement).replace("\\", "/");
        String url = StaticConfig.RemoteNotifyUrl.replace("{JYLSH}", fallen.getJylsh()).replace("{ZPLX}", fallen.getZplx()).replace("{ZPDZ}", res);
        String s = HttpUtil.get(url).replace("null(", "").replace(")", "");
        System.out.println(s.trim()+"-"+res);
        Callback callback = gson.fromJson(s, Callback.class);
        if ("0".equalsIgnoreCase(callback.getRes())) {
            log.error("流水号为{}，照片类型为{}的图片上传失败，原因是：{}", fallen.getJylsh(), fallen.getZplx(), callback.getMsg());
            FileUtils.forceDelete(file);
            return JsonResultUtil.resDispatcher(callback.getMsg(), 0);
        } else {
            return JsonResultUtil.resDispatcher(res);
        }
    }
}