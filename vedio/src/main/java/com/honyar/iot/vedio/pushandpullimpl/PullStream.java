package com.honyar.iot.vedio.pushandpullimpl;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

public class PullStream {

    /**
     * 直播拉流实现
     */
    public void getPullStream(String inputPath) throws Exception {
        //创建+设置采集器
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(inputPath);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.setImageWidth(960);
        grabber.setImageHeight(540);

        //开启采集器
        grabber.start();

        //直播播放窗口
        CanvasFrame canvasFrame = new CanvasFrame("德华正在喂饭。。。。。。");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        //播流
        while (true){
            Frame frame = grabber.grabImage();  //拉流
            canvasFrame.showImage(frame);   //播放
        }
    }
}
