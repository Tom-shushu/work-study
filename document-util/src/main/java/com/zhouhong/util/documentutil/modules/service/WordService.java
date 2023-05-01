package com.zhouhong.util.documentutil.modules.service;

import org.springframework.web.multipart.MultipartFile;

public interface WordService {

    /**
     * @description: word转其他文件
     * @return: java.lang.String
     * @author: zhouhong
     * @date: 2023/4/30 11:58
     */
    String wordToFile(MultipartFile file, String type);

}
