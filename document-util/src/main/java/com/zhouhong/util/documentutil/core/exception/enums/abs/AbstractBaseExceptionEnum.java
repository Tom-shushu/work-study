package com.zhouhong.util.documentutil.core.exception.enums.abs;

/**
  * @description: 异常枚举格式规范
  * @author: zhouhong
  * @date: 2023/5/1 22:52
  */
public interface AbstractBaseExceptionEnum {

    /**
     * 获取异常的状态码
     */
    Integer getCode();

    /**
     * 获取异常的提示信息
     */
    String getMessage();

}
