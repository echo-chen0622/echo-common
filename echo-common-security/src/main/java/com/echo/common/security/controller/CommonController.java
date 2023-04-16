package com.echo.common.security.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.core.constant.Constants;
import com.echo.common.security.utils.ServerConfig;
import com.echo.common.web.config.EchoConfig;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.utils.StringUtils;
import com.echo.common.web.utils.file.FileUploadUtils;
import com.echo.common.web.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author echo
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = EchoConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public BaseResponse<Object> uploadFile(MultipartFile file) throws Exception {
        // 上传文件路径
        String filePath = EchoConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = serverConfig.getUrl() + fileName;
        Map<String, Object> ajax = new HashMap<>(4);
        ajax.put("url", url);
        ajax.put("fileName", fileName);
        ajax.put("newFileName", FileUtils.getName(fileName));
        ajax.put("originalFilename", file.getOriginalFilename());
        return BaseResponse.ok(ajax);
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public BaseResponse<Object> uploadFiles(List<MultipartFile> files) throws Exception {
        // 上传文件路径
        String filePath = EchoConfig.getUploadPath();
        List<String> urls = new ArrayList<String>();
        List<String> fileNames = new ArrayList<String>();
        List<String> newFileNames = new ArrayList<String>();
        List<String> originalFilenames = new ArrayList<String>();
        for (MultipartFile file : files) {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            urls.add(url);
            fileNames.add(fileName);
            newFileNames.add(FileUtils.getName(fileName));
            originalFilenames.add(file.getOriginalFilename());
        }
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("urls", CharSequenceUtil.join(FILE_DELIMETER, urls));
        ajax.put("fileNames", CharSequenceUtil.join(FILE_DELIMETER, fileNames));
        ajax.put("newFileNames", CharSequenceUtil.join(FILE_DELIMETER, newFileNames));
        ajax.put("originalFilenames", CharSequenceUtil.join(FILE_DELIMETER, originalFilenames));
        return BaseResponse.ok(ajax);
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (!FileUtils.checkAllowDownload(resource)) {
            throw new ApiException(CharSequenceUtil.format("资源文件({})非法，不允许下载。 ", resource));
        }
        // 本地资源路径
        String localPath = EchoConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + org.apache.commons.lang3.StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = org.apache.commons.lang3.StringUtils.substringAfterLast(downloadPath, "/");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, downloadName);
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
}
