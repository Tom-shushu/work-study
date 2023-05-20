package com.zhouhong.designpattern.strategy.context;

import com.alibaba.fastjson.JSONObject;
import com.zhouhong.designpattern.strategy.service.VoiceStrategyService;
import com.zhouhong.designpattern.strategy.service.impl.AliGenieServiceImpl;
import com.zhouhong.designpattern.strategy.service.impl.DuerOSServiceImpl;
import com.zhouhong.designpattern.strategy.service.impl.RokidServiceImpl;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 语音策略上下文
 * @author: zhouhong
 * @date: 2023/5/20 14:27
 * @version: 1.0
 */
@Data
@Component
public class VoiceStrategyContext {
    @Resource
    private VoiceStrategyService voiceStrategyService;
    public void setVoiceStrategy(VoiceStrategyService voiceStrategyService) {
        this.voiceStrategyService = voiceStrategyService;
    }
    public JSONObject executeStrategy(JSONObject jsonObject) {
        if (voiceStrategyService != null) {
            return voiceStrategyService.operateApi(jsonObject);
        }
        return null;
    }

    /**
      * @description: 根据传过来的KEY值选择具体的策略
      * @return: com.alibaba.fastjson.JSONObject
      * @author: zhouhong
      * @date: 2023/5/20 15:03
      */
    public JSONObject executeStrategyByKey(String key, JSONObject jsonObject) {
        switch (key) {
            case "aliGenie" : {
                this.setVoiceStrategy(new AliGenieServiceImpl());
                return this.executeStrategy(jsonObject);
            }
            case "duerOS" : {
                this.setVoiceStrategy(new DuerOSServiceImpl());
                return this.executeStrategy(jsonObject);
            }
            case "rokid" : {
                this.setVoiceStrategy(new RokidServiceImpl());
                return this.executeStrategy(jsonObject);
            }
            default: {
                return null;
            }
        }
    }
}
