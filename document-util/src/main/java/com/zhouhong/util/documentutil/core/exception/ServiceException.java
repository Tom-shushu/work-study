package com.zhouhong.util.documentutil.core.exception;

import com.zhouhong.util.documentutil.core.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
  * @description: 业务异常
  * @author: zhouhong
  * @date: 2023/5/1 22:51
  */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    private Integer code;

    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }
}
