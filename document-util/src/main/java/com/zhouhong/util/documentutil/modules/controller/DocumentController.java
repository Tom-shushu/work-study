package com.zhouhong.util.documentutil.modules.controller;

import com.zhouhong.util.documentutil.core.response.ResponseData;
import com.zhouhong.util.documentutil.core.response.SuccessResponseData;
import com.zhouhong.util.documentutil.modules.service.ExcelService;
import com.zhouhong.util.documentutil.modules.service.ImageService;
import com.zhouhong.util.documentutil.modules.service.PdfService;
import com.zhouhong.util.documentutil.modules.service.WordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @description: 文档操作类
 * @author: zhouhong
 * @date: 2023/4/28 14:42
 * @version: 1.0
 */
@RestController
public class DocumentController {

    @Resource
    private PdfService pdfService;
    @Resource
    private ImageService imageService;
    @Resource
    private WordService wordService;
    @Resource
    private ExcelService excelService;
    /**
      * @description: PDF转其他文件
      * @return: com.zhouhong.util.documentutil.core.response.ResponseData
      * @author: zhouhong
      * @date: 2023/4/30 11:59
      */
    @PostMapping("/document/utils/pdf/pdftofile")
    public ResponseData pdftofile(@RequestParam("file") MultipartFile file, String type) {
        return new SuccessResponseData(pdfService.pdfToFile(file, type));
    }

    /**
      * @description: 合并两个PDF文件
      * @return: com.zhouhong.util.documentutil.core.response.ResponseData
      * @author: zhouhong
      * @date: 2023/4/30 20:11
      */
    @PostMapping("/document/utils/pdf/mergetwopdf")
    public ResponseData mergeTwoPdfFile(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2) {
        return new SuccessResponseData(pdfService.mergeTwoPdfFile(file1, file2));
    }

    /**
      * @description: 合并多个PDF文件
      * @return: com.zhouhong.util.documentutil.core.response.ResponseData
      * @author: zhouhong
      * @date: 2023/4/30 20:11
      */
    @PostMapping("/document/utils/pdf/mergemorepdf")
    public ResponseData mergeMorePdfFile(@RequestParam("file") MultipartFile[] file) {
        return new SuccessResponseData(pdfService.mergeMorePdfFile(file));
    }

    /**
      * @description: Word转其他文件
      * @return: com.zhouhong.util.documentutil.core.response.ResponseData
      * @author: zhouhong
      * @date: 2023/4/30 11:59
      */
    @PostMapping("/document/utils/word/wordtofile")
    public ResponseData wordtofile(@RequestParam("file") MultipartFile file, String type) {
        return new SuccessResponseData(wordService.wordToFile(file, type));
    }

    /**
     * @description: Excel转其他文件
     * @return: com.zhouhong.util.documentutil.core.response.ResponseData
     * @author: zhouhong
     * @date: 2023/4/30 11:59
     */
    @PostMapping("/document/utils/excel/exceltofile")
    public ResponseData exceltofile(@RequestParam("file") MultipartFile file, String type) {
        return new SuccessResponseData(excelService.excelToFile(file, type));
    }

    /**
      * @description: 文件转图片
      * @return: com.zhouhong.util.documentutil.core.response.ResponseData
      * @author: zhouhong
      * @date: 2023/4/30 12:00
      */
    @PostMapping("/document/utils/images/filetoimages")
    public ResponseData filetoimages(@RequestParam("file") MultipartFile file) {
        return new SuccessResponseData(imageService.fileToImage(file));
    }

}
