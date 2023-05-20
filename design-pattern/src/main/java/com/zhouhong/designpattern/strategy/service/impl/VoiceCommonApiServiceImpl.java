package com.zhouhong.designpattern.strategy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhouhong.designpattern.strategy.context.VoiceStrategyContext;
import com.zhouhong.designpattern.strategy.service.VoiceCommonApiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 语音公共方法抽离
 * @author: zhouhong
 * @date: 2023/5/17 11:00
 * @version: 1.0
 */
@Service
@Log4j2
public class VoiceCommonApiServiceImpl implements VoiceCommonApiService {
    @Resource
    private VoiceStrategyContext voiceStrategyContext;
    @Override
    public JSONObject voiceRequestBodyCommonApi(JSONObject jsonObject, String platform) {
        log.info("大量逻辑校验代码......");
        voiceStrategyContext.executeStrategyByKey(platform, jsonObject);
        log.info("其他业务代码......");
        return null;
    }
}
