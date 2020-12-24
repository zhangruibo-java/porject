package com.mr.common.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MrException extends RuntimeException {
    //自定义参数异常
   /* private Integer code;

    private  String message;*/
   private ExceptionEnums exceptionEnums;

}
