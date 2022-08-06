package com.zhouhong.server1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    /**
     * 服务名称和接口第一段字符相同
     * @return
     */
    @PostMapping("/server1/test")
    public String theSameServer() {
        return "你好呀( ⊙ o ⊙ )！";
    }

    /**
     * 服务名称和接口第一段字符不相同
     * @return
     */
    @PostMapping("/serverNotSame/test")
    public String gettoken() {
        return "你好呀( ⊙ o ⊙ )！";
    }

}
