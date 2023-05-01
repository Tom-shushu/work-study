package com.zhouhong.util.documentutil.core.response;

import lombok.Data;

/**
  * @description: 响应结果数据
  * @author: zhouhong
  * @date: 2023/5/1 22:52
  */
@Data
public class ResponseData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 本地语言响应信息
     */
    private String localizedMsg;

    /**
     * 响应对象
     */
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Boolean success, Integer code, String message, String localizedMsg, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.localizedMsg = localizedMsg;
        this.data = data;
    }

    public ResponseData(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static SuccessResponseData success() {
        return new SuccessResponseData();
    }

    public static SuccessResponseData success(Object object) {
        return new SuccessResponseData(object);
    }

    public static SuccessResponseData success(Integer code, String message, Object object) {
        return new SuccessResponseData(code, message, object);
    }

    public static SuccessResponseData success(Integer code, String message) {
        return new SuccessResponseData(code, message);
    }

    public static SuccessResponseData success(Integer code, String message, String localizedMsg, Object object) {
        return new SuccessResponseData(code, message, localizedMsg, object);
    }

    public static ErrorResponseData error(String message) {
        return new ErrorResponseData(message);
    }

    public static ErrorResponseData error(Integer code, String message) {
        return new ErrorResponseData(code, message);
    }

    public static ErrorResponseData error(Integer code, String message, Object object) {
        return new ErrorResponseData(code, message, object);
    }

    public static ErrorResponseData error(Integer code, String message, String localizedMsg, Object object) {
        return new ErrorResponseData(code, message, localizedMsg, object);
    }
}
