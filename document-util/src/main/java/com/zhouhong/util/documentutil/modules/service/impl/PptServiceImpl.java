package com.zhouhong.util.documentutil.modules.service.impl;

import com.aspose.slides.Presentation;
import com.zhouhong.util.documentutil.core.exception.ServiceException;
import com.zhouhong.util.documentutil.core.utils.OssUpLoadTools;
import com.zhouhong.util.documentutil.modules.service.PptService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/5/1 22:16
 * @version: 1.0
 */
@Log4j2
@Service
public class PptServiceImpl implements PptService {

    @Resource
    private OssUpLoadTools ossUpLoadTools;

    @Override
    public String PptToFile(MultipartFile file, String type) {
        // 获取文件后缀名
        String checkType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"ppt".equals(checkType) && !"pptx".equals(checkType)) {
            throw new ServiceException(1, "输入文件不是PPT文件！");
        }
        try {
            switch (type.toUpperCase()) {
                case "HTML" : {
                    return SwitchFile(file, com.aspose.slides.SaveFormat.Html, "html");
                }
                case "HTML5" : {
                    return SwitchFile(file, com.aspose.slides.SaveFormat.Html5, "html");
                }
                case "PDF" : {
                    return SwitchFile(file, com.aspose.slides.SaveFormat.Pdf, "pdf");
                }
                default:{}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String SwitchFile(MultipartFile file, int saveFormat, String suffix) {
        String url = "";
        try {
            long old = System.currentTimeMillis();
            String fileName = UUID.randomUUID() + "." + suffix;
            String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
            FileOutputStream os = new FileOutputStream(filePath);
            //加载源文件数据
            Presentation ppt = new Presentation(file.getInputStream());
            //设置转换文件类型并转换
            ppt.save(os, saveFormat);
            os.close();
            File outputfile  = new File(filePath);
            url = ossUpLoadTools.uploadOssFile(fileName, outputfile);
            // 删除临时文件
            outputfile.delete();
            long now = System.currentTimeMillis();
            log.info("共耗时：" + ((now - old) / 1000.0) + "秒");
            return url;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


}
