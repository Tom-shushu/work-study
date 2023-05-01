package com.zhouhong.util.documentutil.modules.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {


    /**
     * @description: txt、PDF、WORD、PPT、PPTX文件转图片
     * @return: java.util.List<java.lang.String>
     * @author: zhouhong
     * @date: 2023/4/30 11:58
     */
    List<String> fileToImage(MultipartFile file);

}
