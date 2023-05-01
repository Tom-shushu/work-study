package com.zhouhong.util.documentutil.modules.service.impl;

import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.pdf.facades.PdfFileEditor;
import com.zhouhong.util.documentutil.core.exception.ServiceException;
import com.zhouhong.util.documentutil.core.utils.OssUpLoadTools;
import com.zhouhong.util.documentutil.modules.service.PdfService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aspose.pdf.Document;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: PDF操作类
 * @author: zhouhong
 * @date: 2023/4/28 14:43
 * @version: 1.0
 */
@Service
@Log4j2
public class PdfServiceImpl implements PdfService {
    @Resource
    private OssUpLoadTools ossUpLoadTools;

    /**
      * @description: PDF 转其他文件
      * @return: java.util.List<java.lang.String>
      * @author: zhouhong
      * @date: 2023/5/1 23:34
      */
    @Override
    public List<String> pdfToFile(MultipartFile file,String type) {
        List<String> res = new ArrayList<>();
        String checkType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"pdf".equals(checkType)) {
            throw new ServiceException(1, "输入文件不是PDF文件！");
        }
        try {
            switch (type.toUpperCase()) {
                case "WORD" : {
                    return switchFile(file, com.aspose.pdf.SaveFormat.DocX, "docx");
                }
                case "XML" : {
                    return switchFile(file, SaveFormat.PdfXml, "xml");
                }
                case "EXCEL" : {
                    return switchFile(file, com.aspose.pdf.SaveFormat.Excel, "xlsx");
                }
                case "PPT" : {
                    return switchFile(file, com.aspose.pdf.SaveFormat.Pptx, "pptx");
                }
                case "PNG" : {
                    // 图片类型的需要获取每一页PDF，一张一张转换
                    Document pdfDocument = new Document(file.getInputStream());
                    //分辨率
                    Resolution resolution = new Resolution(130);
                    PngDevice pngDevice = new PngDevice(resolution);
                    //
                    if (pdfDocument.getPages().size() <= 10) {
                        for (int index = 0; index < pdfDocument.getPages().size(); index++) {
                            String fileName = UUID.randomUUID() + ".png";
                            String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
                            File tmpFile = new File(filePath);
                            FileOutputStream fileOS = new FileOutputStream(tmpFile);
                            pngDevice.process(pdfDocument.getPages().get_Item(index), fileOS);
                            res.add(ossUpLoadTools.uploadOssFile(fileName, tmpFile));
                            fileOS.close();
                            tmpFile.delete();
                        }
                    } else {
                        throw new ServiceException(2, "抱歉超过10页暂时无法转图片");
                    }
                    return res;
                }
                case "HTML" : {
                    String fileName = UUID.randomUUID() + ".html";
                    String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
                    Document doc = new Document(file.getInputStream());

                    HtmlSaveOptions saveOptions = new HtmlSaveOptions();
                    saveOptions.setFixedLayout(true);
                    saveOptions.setSplitIntoPages(false);
                    saveOptions.setRasterImagesSavingMode(HtmlSaveOptions.RasterImagesSavingModes.AsExternalPngFilesReferencedViaSvg);
                    doc.save(filePath , saveOptions);
                    doc.close();
                    File outputfile  = new File(filePath);
                    res.add(ossUpLoadTools.uploadOssFile(fileName, outputfile));
                    outputfile.delete();
                    return res;
                }
                default:{}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
      * @description: 合并两个PDF文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/5/1 23:40
      */
    @Override
    public String mergeTwoPdfFile(MultipartFile  file1, MultipartFile file2) {
        try {
            Document doc1 = new Document(file1.getInputStream());
            Document doc2 = new Document(file2.getInputStream());
            doc1.getPages().add(doc2.getPages());

            String fileName = UUID.randomUUID() + ".pdf";
            String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
            doc1.save(filePath);
            doc1.close();
            File outputfile  = new File(filePath);
            String res = ossUpLoadTools.uploadOssFile(fileName, outputfile);
            outputfile.delete();
            return res;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
      * @description:  合并对个PDF文件
      * @return: java.lang.String
      * @author: zhouhong
      * @date: 2023/5/1 23:40
      */
    @Override
    public String mergeMorePdfFile(MultipartFile ... file) {
        try {
            String mergeFileName = UUID.randomUUID() + ".pdf";
            String mergePdfPath = ossUpLoadTools.getSavePath() + "/"  + mergeFileName;
            String[] chilPdfPath = new String[file.length];
            // 读取PDF并获取路径
            for (int i = 0; i < file.length; i++) {
                String fileName = UUID.randomUUID() + ".pdf";
                String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
                FileOutputStream os = new FileOutputStream(filePath);
                Document doc = new Document(file[i].getInputStream());
                doc.save(os);
                chilPdfPath[i] = filePath;
                os.close();
                doc.close();
            }
            // 合并多个PDF
            PdfFileEditor pdfFileEditor = new PdfFileEditor();
            pdfFileEditor.concatenate(chilPdfPath, mergePdfPath);

            // 读取文件上传OSS
            File outputfile  = new File(mergePdfPath);
            String resUrl = ossUpLoadTools.uploadOssFile(mergeFileName, outputfile);
            outputfile.delete();
            return resUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<String> switchFile(MultipartFile file, SaveFormat saveFormat, String suffix) {
        List<String> resUrl = new ArrayList<>();
        try {
            long old = System.currentTimeMillis();
            // 输出路径
            String fileName = UUID.randomUUID() + "." + suffix;
            String filePath = ossUpLoadTools.getSavePath() + "/" + fileName;
            FileOutputStream os = new FileOutputStream(filePath);
            Document doc = new Document(file.getInputStream());
            doc.save(os, saveFormat);
            os.close();
            doc.close();
            File outputfile  = new File(filePath);
            resUrl.add(ossUpLoadTools.uploadOssFile(fileName, outputfile));
            outputfile.delete();
            long now = System.currentTimeMillis();
            log.info("共耗时：" + ((now - old) / 1000.0) + "秒");

        }catch (IOException e) {
            e.printStackTrace();
        }
        return resUrl;
    }

}
