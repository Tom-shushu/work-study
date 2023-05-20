package com.zhouhong.designpattern.strategy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhouhong.designpattern.strategy.service.VoiceStrategyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zhouhong
 * @date: 2023/5/20 14:31
 * @version: 1.0
 */
@Service
@Log4j2
public class DuerOSServiceImpl implements VoiceStrategyService {
    @Override
    public JSONObject operateApi(JSONObject jsonObject) {
        log.info("小度-设备发现/控制成功！");
        return null;
    }
}
