package com.honyar.iot.vedio.start;

import com.honyar.iot.vedio.pushandpullimpl.PushStream;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * 直播推流--启动
 */
@SpringBootApplication
@Configuration
public class PushApplication {
    public static void main(String[] args) throws Exception {
        //设置rtmp服务器推流地址（视频流服务器公网地址）
        String outputPath = "rtmp://xxx.xx.xx.xx:1935/live/address";
        PushStream recordPush = new PushStream();
        recordPush.getRecordPush(outputPath, 25);
    }
}