package com.zhouhong.iotdbdemo.response;

/**
 * description: 返回结果封装
 * date: 2022/8/15 21:32
 * author: zhouhong
 */
public class ResponseData {
    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";
    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";
    public static final Integer DEFAULT_SUCCESS_CODE = 200;
    public static final Integer DEFAULT_ERROR_CODE = 500;
    private Boolean success;
    private Integer code;
    private String message;
    private String localizedMsg;
    private Object data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Boolean success, Integer code, String message, String localizedMsg, Object data) {
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

    public Boolean getSuccess() {
        return this.success;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getLocalizedMsg() {
        return this.localizedMsg;
    }

    public Object getData() {
        return this.data;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setLocalizedMsg(final String localizedMsg) {
        this.localizedMsg = localizedMsg;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResponseData)) {
            return false;
        } else {
            ResponseData other = (ResponseData)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$success = this.getSuccess();
                    Object other$success = other.getSuccess();
                    if (this$success == null) {
                        if (other$success == null) {
                            break label71;
                        }
                    } else if (this$success.equals(other$success)) {
                        break label71;
                    }

                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                label57: {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        if (other$message == null) {
                            break label57;
                        }
                    } else if (this$message.equals(other$message)) {
                        break label57;
                    }

                    return false;
                }

                Object this$localizedMsg = this.getLocalizedMsg();
                Object other$localizedMsg = other.getLocalizedMsg();
                if (this$localizedMsg == null) {
                    if (other$localizedMsg != null) {
                        return false;
                    }
                } else if (!this$localizedMsg.equals(other$localizedMsg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data == null) {
                        return true;
                    }
                } else if (this$data.equals(other$data)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResponseData;
    }

    public int hashCode() {
        int result1 = 1;
        Object $success = this.getSuccess();
        int result = result1 * 59 + ($success == null ? 43 : $success.hashCode());
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $localizedMsg = this.getLocalizedMsg();
        result = result * 59 + ($localizedMsg == null ? 43 : $localizedMsg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "ResponseData(success=" + this.getSuccess() + ", code=" + this.getCode() + ", message=" + this.getMessage() + ", localizedMsg=" + this.getLocalizedMsg() + ", data=" + this.getData() + ")";
    }
}