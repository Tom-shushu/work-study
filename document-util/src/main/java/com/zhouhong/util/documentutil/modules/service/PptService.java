package com.zhouhong.util.documentutil.modules.service;

import org.springframework.web.multipart.MultipartFile;

public interface PptService {

    /**
      * @description: PPT 转 其他文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/5/1 22:18
      */
    String PptToFile(MultipartFile file, String type);

}
