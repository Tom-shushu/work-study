package com.zhouhong.util.documentutil.core.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
  * @description: 失败响应结果
  * @author: zhouhong
  * @date: 2023/5/1 22:52
  */
@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponseData extends ResponseData {

    /**
     * 异常的具体类名称
     */
    private String exceptionClazz;

    ErrorResponseData(String message) {
        super(false, DEFAULT_ERROR_CODE, message, message, null);
    }

    public ErrorResponseData(Integer code, String message) {
        super(false, code, message, message, null);
    }

    ErrorResponseData(Integer code, String message, Object object) {
        super(false, code, message, object);
    }

    ErrorResponseData(Integer code, String message, String localizedMsg, Object object) {
        super(false, code, message, localizedMsg, object);
    }

}
