package com.zhouhong.util.documentutil.modules.service.impl;

import com.aspose.cells.Workbook;
import com.zhouhong.util.documentutil.core.exception.ServiceException;
import com.zhouhong.util.documentutil.core.utils.OssUpLoadTools;
import com.zhouhong.util.documentutil.modules.service.ExcelService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/4/30 12:48
 * @version: 1.0
 */
@Service
@Log4j2
public class ExcelServiceImpl implements ExcelService {
    @Resource
    private OssUpLoadTools ossUpLoadTools;

    /**
      * @description: Excel转其他文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/5/1 23:44
      */
    @Override
    public String excelToFile(MultipartFile file, String type) {
        String checkType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"xlsx".equals(checkType) && !"xls".equals(checkType)) {
            throw new ServiceException(1, "输入文件不是Excel文件！");
        }
        try {
            switch (type.toUpperCase()) {
                /******************** 文档类型 ***************/
                case "WORD" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.DOCX, "docx");
                }
                case "PDF" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.PDF, "pdf");
                }
                case "PPT" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.PPTX, "pptx");
                }
                case "HTML" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.HTML, "html");
                }
                case "JSON" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.JSON, ".json");
                }
                case "MARKDOWN" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.MARKDOWN, "md");
                }
                /***************** 图片类型 （注意图片格式的默认只转换第一个 Sheet1）*********************/
                case "PNG" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.PNG, "png");
                }
                case "JPG" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.JPG, "jpg");
                }
                case "BMP" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.BMP, "bmp");
                }
                case "CSV" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.CSV, "csv");
                }
                case "SVG" : {
                    return SwitchFile(file, com.aspose.cells.SaveFormat.SVG, "svg");
                }
                // 好像有问题，有需要大家自己调试一下
//                case "XML" : {
//                    return SwitchFile(file, com.aspose.cells.SaveFormat.XML, "xml");
//                }
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
            Workbook excel = new Workbook(file.getInputStream());
            //设置转换文件类型并转换
            excel.save(os, saveFormat);
            os.close();
            File outputfile  = new File(filePath);
            url = ossUpLoadTools.uploadOssFile(fileName, outputfile);
            outputfile.delete();
            long now = System.currentTimeMillis();
            log.info("共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


}
