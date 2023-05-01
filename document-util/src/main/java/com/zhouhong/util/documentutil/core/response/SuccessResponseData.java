package com.zhouhong.util.documentutil.core.response;

/**
  * @description: 成功响应结果
  * @author: zhouhong
  * @date: 2023/5/1 22:53
  */
public class SuccessResponseData<T> extends ResponseData {

    public SuccessResponseData() {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public SuccessResponseData(T object) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, DEFAULT_SUCCESS_MESSAGE, object);
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
