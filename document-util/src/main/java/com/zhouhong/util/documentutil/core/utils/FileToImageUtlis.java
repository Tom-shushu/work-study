package com.zhouhong.util.documentutil.core.utils;

import com.aspose.cells.*;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileToImageUtlis {

    /**
     * word和txt文件转换图片
     * @param inputStream
     * @return
     */
    public List<BufferedImage> wordAndTxtToImg(InputStream inputStream) {
        try {
            Document doc = new Document(inputStream);
            ImageSaveOptions options = new ImageSaveOptions(SaveFormat.PNG);
            options.setPrettyFormat(true);
            options.setUseAntiAliasing(true);
            options.setUseHighQualityRendering(true);
            int pageCount = doc.getPageCount();
            List<BufferedImage> imageList = new ArrayList<BufferedImage>();
            for (int i = 0; i < pageCount; i++) {
                OutputStream output = new ByteArrayOutputStream();
                doc.save(output, options);
                ImageInputStream imageInputStream = javax.imageio.ImageIO.createImageInputStream(parse(output));
                imageList.add(javax.imageio.ImageIO.read(imageInputStream));
            }
            return imageList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: ppt, pptx文件转换图片
     */
    public List<BufferedImage> pptAndPptxToimg(InputStream inputStream) {
        FileOutputStream out = null;
        try {
            List<BufferedImage> imageList = new ArrayList<BufferedImage>();
            Presentation pres = new Presentation(inputStream);
            ISlideCollection slides = pres.getSlides();
            for (int i = 0; i < slides.size(); i++) {
                ISlide slide = slides.get_Item(i);
                int height = (int) (pres.getSlideSize().getSize().getHeight() - 150);
                int width = (int) (pres.getSlideSize().getSize().getWidth() - 150);
                BufferedImage img = slide.getThumbnail(new java.awt.Dimension(width, height));
                imageList.add(img);
            }
            return imageList;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @Description: pdf文件转换图片
     */
    public List<BufferedImage> pdfToimg(InputStream inputStream) {
        try {
            PDDocument pdDocument;
            pdDocument = PDDocument.load(inputStream);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            int pages = pdDocument.getNumberOfPages();
            List<BufferedImage> imageList = new ArrayList<BufferedImage>();
            for (int i = 0; i < pages; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                imageList.add(image);
            }
            return imageList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @Description: excel文件转换图片
     */
    public List<BufferedImage> excelToImg(InputStream inputStream) {
        Workbook book = null;
        List<BufferedImage> imageList = new ArrayList<BufferedImage>();
        try {
            book = new Workbook(inputStream);
            WorksheetCollection worksheets = book.getWorksheets();
            ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
            imgOptions.setChartImageType(ImageFormat.getJpeg());
            imgOptions.setCellAutoFit(true);
            imgOptions.setOnePagePerSheet(true);
            for (int i = 0; i < worksheets.getCount(); i++) {
                Worksheet sheet = worksheets.get(i);
                OutputStream output = new ByteArrayOutputStream();
                sheet.getPageSetup().setLeftMargin(-20);
                sheet.getPageSetup().setRightMargin(0);
                sheet.getPageSetup().setBottomMargin(0);
                sheet.getPageSetup().setTopMargin(0);
                SheetRender render = new SheetRender(sheet, imgOptions);
                render.toImage(0, output);
                ByteArrayInputStream parse = parse(output);
                if (parse != null) {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(parse);
                    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                    imageList.add(bufferedImage);
                    parse.close();
                }
                output.close();
            }
            return imageList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * @Description: outputStream转inputStream
     */
    public static ByteArrayInputStream parse(OutputStream out) {
        try {
            ByteArrayOutputStream baos = null;
            baos = (ByteArrayOutputStream) out;
            ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
            return swapStream;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}