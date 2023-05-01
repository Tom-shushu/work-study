package com.zhouhong.util.documentutil.modules.service.impl;

import com.zhouhong.util.documentutil.core.exception.ServiceException;
import com.zhouhong.util.documentutil.core.utils.FileToImageUtlis;
import com.zhouhong.util.documentutil.core.utils.OssUpLoadTools;
import com.zhouhong.util.documentutil.modules.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/4/30 12:40
 * @version: 1.0
 */
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private OssUpLoadTools ossUpLoadTools;
    @Resource
    private FileToImageUtlis fileToImageUtlis;

    @Override
    public List<String> fileToImage(MultipartFile file) {
        List<String> resUrlList = new ArrayList<>();
        List<BufferedImage> bufferedImages = new ArrayList<>();
        String type = FilenameUtils.getExtension(file.getOriginalFilename());
        if ("txt".equals(type) || "docx".equals(type) || "doc".equals(type) || "pptx".equals(type)
                || "pdf".equals(type) || "xlsx".equals(type) || "xls".equals(type)) {
            try {
                switch (type) {
                    case "txt" :
                    case "docx" :
                    case "doc" : {
                        bufferedImages = fileToImageUtlis.wordAndTxtToImg(file.getInputStream());
                        break;
                    }
                    case "pptx" : {
                        bufferedImages = fileToImageUtlis.pptAndPptxToimg(file.getInputStream());
                        break;
                    }
                    case "pdf" : {
                        bufferedImages = fileToImageUtlis.pdfToimg(file.getInputStream());
                        break;
                    }
                    case "xlsx" :
                    case "xls" : {
                        bufferedImages = fileToImageUtlis.excelToImg(file.getInputStream());
                        break;
                    }
                    default:{}
                }
            } catch (Exception e) {
            }
            bufferedImages.forEach(image -> {
                try {
                    String imageName = UUID.randomUUID() + ".png";
                    String linuxFilePath = ossUpLoadTools.getSavePath() + imageName;
                    File outputfile  = new File(linuxFilePath);
                    ImageIO.write(image,  "png",  outputfile);
                    // 上传阿里云OSS
                    String ossUrl = ossUpLoadTools.uploadOssFile(imageName, outputfile);
                    resUrlList.add(ossUrl);
                    outputfile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            throw new ServiceException(2, "抱歉，目前只支持Excel、PPT、Word、txt、PDF转图片");
        }
        return resUrlList;
    }

}
