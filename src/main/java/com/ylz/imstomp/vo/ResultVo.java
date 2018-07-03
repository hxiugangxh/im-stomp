package com.ylz.imstomp.vo;

import lombok.Data;

/**
 * 统一返回结果pojo
 */
@Data
public class ResultVo<T> {

    private ResultStatus status;
    private String msg;
    private T data;

    public ResultVo(ResultStatus status) {
        this(status, status.getReasonPhrase(), null);
    }

    public ResultVo(ResultStatus status, T data) {
        this(status, status.getReasonPhrase(), data);
    }

    public ResultVo(ResultStatus status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
