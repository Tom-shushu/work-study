package com.zhouhong.util.documentutil.modules.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/4/28 14:43
 * @version: 1.0
 */
public interface PdfService {

    /**
     * @description:  PDF 转 Word
     * @param: [file]
     * @return: org.springframework.web.multipart.MultipartFile
     * @author: zhouhong
     * @date: 2023/4/28 14:48
     */
    List<String> pdfToFile(MultipartFile file, String type);

    /**
      * @description:  合并两个PDF文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/4/30 19:10
      */
    String mergeTwoPdfFile(MultipartFile file1, MultipartFile file2);

    /**
      * @description: 合并多个PDF文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/4/30 19:30
      */
    String mergeMorePdfFile(MultipartFile ... file);

}
