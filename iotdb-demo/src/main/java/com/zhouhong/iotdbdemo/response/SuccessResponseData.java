package com.zhouhong.iotdbdemo.response;
/**
 * description: 正确返回结果封装
 * date: 2022/8/15 21:40
 * author: zhouhong
 */

public class SuccessResponseData extends ResponseData {
    public SuccessResponseData() {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", "请求成功", (Object)null);
    }

    public SuccessResponseData(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", "请求成功", object);
    }

    public SuccessResponseData(Integer code, String message, Object object) {
        super(true, code, message, message, object);
    }

    public SuccessResponseData(Integer code, String message, String localizedMsg, Object object) {
        super(true, code, message, localizedMsg, object);
    }

    public SuccessResponseData(Integer code, String message) {
        super(true, code, message);
    }
}