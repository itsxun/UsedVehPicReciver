package xin.fallen.controller;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.fallen.config.StaticConfig;
import xin.fallen.domain.Fallen;
import xin.fallen.domain.FallenPlus;
import xin.fallen.util.Base64Util;
import xin.fallen.util.HttpUtil;
import xin.fallen.util.JsonResultUtil;
import xin.fallen.vo.Callback;
import xin.fallen.vo.JsonResult;

@RestController
public class PicUploadCtrl {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping({"/new"})
    public JsonResult newRecord(HttpServletRequest req, @NotEmpty String jylsh, @NotEmpty String zplx, HttpServletResponse resp) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        Gson gson = new Gson();
        Date date = new Date();
        String values = IOUtils.toString(req.getInputStream());
        Fallen fallen = gson.fromJson(values, Fallen.class);
        if (fallen != null || fallen.getJylsh() != null || fallen.getZplx() != null || fallen.getPic() != null) {
            return JsonResultUtil.resDispatcher("请提供正确的交易流水号与照片类型", 0);
        }
        File file = new File(StaticConfig.WanDirPath + File.separator + sdf.format(date) + File.separator + fallen.getJylsh());
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        file = new File(file + File.separator + fallen.getZplx() + "_" + UUID.randomUUID().toString().toUpperCase() + ".JPG");
        FileUtils.writeByteArrayToFile(file, Base64Util.decode(fallen.getPic().replace(StaticConfig.Base64EncodePrefix, "")));

        String res = file.getAbsolutePath().replace(StaticConfig.WanPathReplacement, StaticConfig.WanUrlReplacement).replace("\\", "/");
        res = StaticConfig.RemoteNotifyUrl.replace("{JYLSH}", fallen.getJylsh()).replace("{ZPLX}", fallen.getZplx()).replace("{ZPDZ}", res);
        res = HttpUtil.get(res).replace("null(", "").replace(")", "");

        Callback callback = gson.fromJson(res, Callback.class);
        if ("0".equalsIgnoreCase(callback.getRes())) {
            this.log.error("流水号为{}，照片类型为{}的图片上传失败，原因是：{}", new Object[]{fallen.getJylsh(), fallen.getZplx(), callback.getMsg()});
            FileUtils.forceDelete(file);
            return JsonResultUtil.resDispatcher(callback.getMsg(), 0);
        } else {
            return JsonResultUtil.resDispatcher(res);
        }
    }

    @RequestMapping({"/pre"})
    public JsonResult newRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        Gson gson = new Gson();
        Date date = new Date();
        FallenPlus fallen = gson.fromJson(IOUtils.toString(req.getInputStream()), FallenPlus.class);
        if (fallen == null || fallen.getZplx() == null || fallen.getPic() == null || fallen.getDjzsbh() == null || fallen.getHphm() != null || fallen.getHpzl() == null || fallen.getXszbh() == null) {
            return JsonResultUtil.resDispatcher("请填写完整有效信息", 0);
        }
        File file = new File(StaticConfig.WanDirPathPre + File.separator + sdf.format(date));
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = new File(file + File.separator + sdf.format(date) + "_" + UUID.randomUUID().toString().toUpperCase() + ".JPG");
        FileUtils.writeByteArrayToFile(file, Base64Util.decode(fallen.getPic().replace(StaticConfig.Base64EncodePrefix, "")));

        String res = file.getAbsolutePath().replace(StaticConfig.WanPathReplacementPre, StaticConfig.WanUrlReplacementPre).replace("\\", "/");
        res = StaticConfig.RemoteNotifyUrlPre.replace("{HPZL}", fallen.getHpzl()).replace("{ZPLX}", fallen.getZplx()).replace("{HPHM}", fallen.getHphm()).replace("{DJZSBH}", fallen.getDjzsbh()).replace("{XSZBH}", fallen.getXszbh()).replace("{DISK_ADDRESS}", file.getAbsolutePath()).replace("{NET_ADDRESS}", res);
        Callback callback = gson.fromJson(HttpUtil.get(res).replace("null(", "").replace(")", ""), Callback.class);
        if ("0".equalsIgnoreCase(callback.getRes())) {
            this.log.error("操作失败，原因是：{}", callback.getMsg());
            FileUtils.forceDelete(file);
            return JsonResultUtil.resDispatcher(callback.getMsg(), 0);
        } else {
            return JsonResultUtil.resDispatcher(res);
        }
    }
}
