package com.honyar.iot.vedio.pushandpullimpl;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import javax.swing.*;
public class PushStream {

    /**
     * 直播推流实现
     */
    public void getRecordPush(String outputPath, int v_rs) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception, InterruptedException {
            //创建采集器
            OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);  //本地摄像头默认为0
            //开启采集器
            try {
                grabber.start();
            } catch (Exception e) {
                try {
                    grabber.restart();  //一次重启尝试
                } catch (Exception e2) {
                    throw e;
                }
            }
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();  //转换器
            Frame grabframe = grabber.grab();  //获取一帧
            IplImage grabbedImage = null;
            if (grabframe != null) {
                grabbedImage = converter.convert(grabframe); //将这一帧转换为IplImage
            }
            //创建录制器
            FrameRecorder recorder;
            recorder = FrameRecorder.createDefault(outputPath, 1280, 720);   //输出路径，画面高，画面宽
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);  //设置编码格式
            recorder.setFormat("flv");
            recorder.setFrameRate(v_rs);
            recorder.setGopSize(v_rs);

            //开启录制器
            try {
                recorder.start();
            } catch (java.lang.Exception e) {
                try {
                    if (recorder != null) {  //尝试重启录制器
                        recorder.stop();
                        recorder.start();
                    }
                } catch (java.lang.Exception e1) {
                    e.printStackTrace();
                }
            }

            //直播效果展示窗口
            CanvasFrame frame = new CanvasFrame("主播-菜鸡-德华", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(true);

            //推流
            while (frame.isVisible() && (grabframe = grabber.grab()) != null) {
                frame.showImage(grabframe);   //展示直播效果
                grabbedImage = converter.convert(grabframe);
                Frame rotatedFrame = converter.convert(grabbedImage);

                if (rotatedFrame != null) {
                    recorder.record(rotatedFrame);
                }

                Thread.sleep(50);  //50毫秒/帧
            }
        }
    }