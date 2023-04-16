package com.echo.common.security.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.echo.common.cache.config.MyCache;
import com.echo.common.security.service.SysConfigService;
import com.echo.common.web.config.EchoConfig;
import com.echo.common.web.response.BaseResponse;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码操作处理
 *
 * @author echo
 */
@RestController
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private MyCache myCache;

    @Autowired
    private SysConfigService configService;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public BaseResponse<Object> getCode(HttpServletResponse response) throws IOException {
        Map<String, Object> ajax = new HashMap<>();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return BaseResponse.ok(ajax);
        }

        // 保存验证码信息
        String uuid = UUID.fastUUID().toString();
        String verifyKey = "CAPTCHA_CODE_KEY::" + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = EchoConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        myCache.saveCache(verifyKey, code);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResponse.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return BaseResponse.ok(ajax);
    }
}
