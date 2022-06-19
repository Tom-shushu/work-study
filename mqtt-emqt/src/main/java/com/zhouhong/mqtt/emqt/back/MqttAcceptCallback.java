package com.zhouhong.mqtt.emqt.back;

import com.zhouhong.mqtt.emqt.client.MqttAcceptClient;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * description: 接收消息后的回调
 * date: 2022/6/16 15:52
 *
 * @author: zhouhong
 */
@Component
@Log4j2
public class MqttAcceptCallback implements MqttCallbackExtended {

    @Resource
    private MqttAcceptClient mqttAcceptClient;

    /**
     * 客户端断开后触发
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("接收消息回调:  连接断开，可以做重连");
        if (MqttAcceptClient.client == null || !MqttAcceptClient.client.isConnected()) {
            log.info("接收消息回调:  emqx重新连接....................................................");
            mqttAcceptClient.reconnection();
        }
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("接收消息回调:  接收消息主题 : " + topic);
        log.info("接收消息回调:  接收消息内容 : " + new String(mqttMessage.getPayload()));
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        String[] topics = token.getTopics();
        for (String topic : topics) {
            log.info("接收消息回调:  向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = token.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, "UTF-8");
            log.info("接收消息回调:  消息的内容是：" + s);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接emq服务器后触发
     *
     * @param b
     * @param s
     */
    @Override
    public void connectComplete(boolean b, String s) {
        log.info("--------------------ClientId:"
                + MqttAcceptClient.client.getClientId() + "客户端连接成功！--------------------");
        // 以/#结尾表示订阅所有以test开头的主题
        // 订阅所有机构主题
        mqttAcceptClient.subscribe("topic111", 0);
    }
}
