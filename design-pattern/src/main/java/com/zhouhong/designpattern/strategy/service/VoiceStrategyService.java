package com.zhouhong.designpattern.strategy.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 语音策略接口 ---  这里使用策略模式可以减少后面调用的代码量
 * @author: zhouhong
 * @date: 2023/5/18 9:22
 * @version: 1.0
 */
public interface VoiceStrategyService {

    /**
     * @description:  语音控制API
     * @param: [jsonObject]
     * @return: com.alibaba.fastjson.JSONObject
     * @author: zhouhong
     * @date: 2023/5/18 9:34
     */
    JSONObject operateApi(@RequestBody JSONObject jsonObject);

}
