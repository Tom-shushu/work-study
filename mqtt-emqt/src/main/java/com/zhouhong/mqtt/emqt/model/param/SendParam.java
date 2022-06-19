package com.zhouhong.mqtt.emqt.model.param;

import lombok.Data;

/**
 * description: 发送消息的参数
 * date: 2022/6/16 16:04
 * @author: zhouhong
 */
@Data
public class SendParam {
    /***
     * 消息内容
     */
    private String messageContent;
    /**
     * 客户端发消息的主题主题
     */
    private String topic;
}
