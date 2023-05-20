package com.zhouhong.designpattern.strategy.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 语音控制公共方法抽取
 * @author: zhouhong
 * @date: 2023/5/17 10:58
 * @version: 1.0
 */
public interface VoiceCommonApiService {

    /**
     * @description:  语音控制公共方法抽取 -- RequestBody格式
     * @param: [jsonObject, platform]
     * @return: com.alibaba.fastjson.JSONObject
     * @author: zhouhong
     * @date: 2023/5/17 11:00
     */
    JSONObject voiceRequestBodyCommonApi(JSONObject jsonObject, String platform);
}
