package com.zhouhong.mqtt.emqt.config;

import com.zhouhong.mqtt.emqt.client.MqttAcceptClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * description: 启动后连接 MQTT 服务器, 监听 mqtt/my_topic 这个topic发送的消息
 * date: 2022/6/16 15:57
 * @author: zhouhong
 */
@Configuration
public class MqttConfig {

    @Resource
    private MqttAcceptClient mqttAcceptClient;

    @Conditional(MqttCondition.class)
    @Bean
    public MqttAcceptClient getMqttPushClient() {
        mqttAcceptClient.connect();
        return mqttAcceptClient;
    }
}
