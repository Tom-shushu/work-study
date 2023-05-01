package com.zhouhong.util.documentutil.modules.service.impl;

import com.aspose.words.SaveFormat;
import com.zhouhong.util.documentutil.core.exception.ServiceException;
import com.zhouhong.util.documentutil.core.utils.OssUpLoadTools;
import com.zhouhong.util.documentutil.modules.service.WordService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @description: Word操作类
 * @author: zhouhong
 * @date: 2023/4/30 12:32
 * @version: 1.0
 */
@Service
@Log4j2
public class WordServiceImpl implements WordService {

    @Resource
    private OssUpLoadTools ossUpLoadTools;
    @Override
    public String wordToFile(MultipartFile file, String type) {
        String checkType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"doc".equals(checkType) && !"docx".equals(checkType)) {
            throw new ServiceException(1, "输入文件不是Word文件！");
        }
        try {
            switch (type.toUpperCase()) {
                case "TEXT" : {
                    return switchFile(file, SaveFormat.TEXT, "txt");
                }
                case "PDF" : {
                    return switchFile(file, com.aspose.words.SaveFormat.PDF, "pdf");
                }
                /*************** 需要操作每一页Word文件，一般Word类的直接电脑操作，应该用不上************/
//                case "PNG" : {
//                    return switchFile(file, com.aspose.words.SaveFormat.PNG, "png");
//                }
//                case "JPG" : {
//                    return switchFile(file, com.aspose.words.SaveFormat.JPEG, "jpg");
//                }
                default:{}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String switchFile(MultipartFile file, int saveFormat, String suffix){
        String url = "";
        try {
            long old = System.currentTimeMillis();
            // 输出路径
            String fileName = UUID.randomUUID() + "." + suffix;
            String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
            FileOutputStream os = new FileOutputStream(filePath);
            com.aspose.words.Document doc = new com.aspose.words.Document(file.getInputStream());
            doc.save(os, saveFormat);
            os.close();
            File outputfile  = new File(filePath);
            url = ossUpLoadTools.uploadOssFile(fileName, outputfile);
            outputfile.delete();
            long now = System.currentTimeMillis();
            log.info("共耗时：" + ((now - old) / 1000.0) + "秒");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

}
