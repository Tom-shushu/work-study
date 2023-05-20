package com.zhouhong.designpattern.strategy.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhouhong.designpattern.strategy.service.VoiceCommonApiService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/5/20 14:44
 * @version: 1.0
 */
@RestController
@RequestMapping("/voice")
public class VoiceController {
    @Resource
    private VoiceCommonApiService voiceCommonApiService;

    @RequestMapping("/aliGenieApi")
    public JSONObject aliGenieOperate(@RequestBody JSONObject jsonObject){
        return voiceCommonApiService.voiceRequestBodyCommonApi(jsonObject, "aliGenie");
    }

    @RequestMapping("/rokidApi")
    public JSONObject rokidOperate(@RequestBody JSONObject jsonObject){
        return voiceCommonApiService.voiceRequestBodyCommonApi(jsonObject, "rokid");
    }

    @RequestMapping("/duerOSApi")
    public JSONObject duerOSOperate(@RequestBody JSONObject jsonObject){
        return voiceCommonApiService.voiceRequestBodyCommonApi(jsonObject, "duerOS");
    }

}
