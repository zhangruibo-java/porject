package com.mr.common.advice;

import lombok.Data;

@Data
public class ExceptionResult {
    private Long dataTime;

    private Integer code;

    private String  message;

    public ExceptionResult(Integer code, String message) {
        this.code = code;
        this.message = message;
        //获取当前时间
        dataTime=System.currentTimeMillis();
    }
}
