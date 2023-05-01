package com.zhouhong.util.documentutil.core.utils;

import java.io.*;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

/**
  * @description: ftp上传下载工具类
  * @author: zhouhong
  * @date: 2023/5/1 22:54
  */
@Component
@Log4j2
public class FtpUtil {

    /**
      * @description: 向FTP服务器上传文件
      * @return: boolean
      * @author: zhouhong
      * @date: 2023/5/1 22:54
      */
    public static boolean uploadFile(String basePath, String filePath, String filename, InputStream input) {
        String host = "xxx.xx.xxx.xx";
        int port = 21;
        String username = "xxxxxxx";
        String password = "xxxxxxxxxxxxx";
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }

            //切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //为了加大上传文件速度，将InputStream转成BufferInputStream
            BufferedInputStream  in=new BufferedInputStream(input);
            //加大缓存区
            ftp.setBufferSize(1024*1024);
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(filename, in)) {
                return result;
            }
            in.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }


    /**
      * @description: 从FTP服务器下载文件
      * @return: boolean
      * @author: zhouhong
      * @date: 2023/5/1 22:53
      */
    public static boolean downloadFile(String remotePath, String fileName, String localPath) {

        String host = "xxx.xx.xxx.xx";
        int port = 21;
        String username = "xxxxxxx";
        String password = "xxxxxxxxxxxxx";

        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

}