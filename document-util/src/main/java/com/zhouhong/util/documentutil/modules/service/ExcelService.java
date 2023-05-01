package com.zhouhong.util.documentutil.modules.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/4/30 12:47
 * @version: 1.0
 */
public interface ExcelService {

    /**
     * @description: Excel转PDF
     * @return: java.lang.String
     * @author: zhouhong
     * @date: 2023/4/30 12:25
     */
    String excelToFile(MultipartFile file, String type);

}
