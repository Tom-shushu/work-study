package com.zhouhong.mqtt.emqt.controller;

import com.zhouhong.mqtt.emqt.client.MqttSendClient;
import com.zhouhong.mqtt.emqt.model.param.SendParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * description:
 * date: 2022/6/16 15:58
 *
 * @author: zhouhong
 */
@RestController
public class SendController {

    @Resource
    private MqttSendClient mqttSendClient;

    @PostMapping("/mqtt/sendmessage")
    public void sendMessage(@RequestBody SendParam sendParam) {
        System.out.println("message:"+sendParam.getMessageContent());
        mqttSendClient.publish(false,"client:report:1",sendParam.getMessageContent());
    }

}
