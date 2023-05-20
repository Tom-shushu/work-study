package com.zhouhong.designpattern.strategy.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhouhong.designpattern.strategy.context.VoiceStrategyContext;
import com.zhouhong.designpattern.strategy.service.impl.AliGenieServiceImpl;
import com.zhouhong.designpattern.strategy.service.impl.DuerOSServiceImpl;
import com.zhouhong.designpattern.strategy.service.impl.RokidServiceImpl;

import javax.annotation.Resource;

/**
 * @description: 测试类
 * @author: zhouhong
 * @date: 2023/5/20 15:06
 * @version: 1.0
 */
public class TextMain {
    public static void main(String[] args) {
        VoiceStrategyContext voiceStrategyContext = new VoiceStrategyContext();
        JSONObject jsonObject = new JSONObject();
        // 天猫精灵
        voiceStrategyContext.setVoiceStrategy(new AliGenieServiceImpl());
        voiceStrategyContext.executeStrategy(jsonObject);
        // 小度
        voiceStrategyContext.setVoiceStrategy(new DuerOSServiceImpl());
        voiceStrategyContext.executeStrategy(jsonObject);
        // 若琪
        voiceStrategyContext.setVoiceStrategy(new RokidServiceImpl());
        voiceStrategyContext.executeStrategy(jsonObject);
    }
}
