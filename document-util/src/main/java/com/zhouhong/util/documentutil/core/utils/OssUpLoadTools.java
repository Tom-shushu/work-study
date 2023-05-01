package com.zhouhong.util.documentutil.core.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.zhouhong.util.documentutil.core.oss.OssConfig;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @description: 阿里云OSS工具类
 * @author: zhouhong
 * @date: 2023/4/30 12:35
 * @version: 1.0
 */
@Component
public class OssUpLoadTools {

    @Resource
    private OssConfig ossConfig;

    /**
      * @description:  获取文件保存地址
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/4/30 12:36
      */
    public String getSavePath() {
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        // 保存目录位置根据项目需求可随意更改
        return applicationHome.getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\templates\\";
    }


    /**
      * @description:  上传文件到阿里云OSS
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/5/1 22:55
      */
    public String uploadOssFile(String fileName, File file){
        // 创建OSSClient实例。
        OSS ossClient = ossConfig.getOssClient();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(),
                    fileName, file);
            putObjectRequest.setProcess("true");
            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            if (result.getResponse().getStatusCode() == 200) {
                return result.getResponse().getUri();
            }
        } catch (OSSException oe) {
        } catch (ClientException ce) {
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

}
