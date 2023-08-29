package com.alias.ai.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * 
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public int getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof com.alias.ai.common.BaseResponse)) {
            return false;
        } else {
            com.alias.ai.common.BaseResponse<?> other = (com.alias.ai.common.BaseResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    return other$message == null;
                } else return this$message.equals(other$message);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof com.alias.ai.common.BaseResponse;
    }

    public String toString() {
        return "BaseResponse(code=" + this.getCode() + ", data=" + this.getData() + ", message=" + this.getMessage() + ")";
    }
}
