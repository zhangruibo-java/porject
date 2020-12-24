package com.mr.common.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//枚举 多个常量类 ,常量的对象 可以设置参数
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnums {

    PRICE_IS_NULL(400,"价格不能为空"),
    CATEGORY_LIST_NOT_FOUNT(401,"参数设置错误"),

    MYSQL_COGNITION_ERROR(500,"mysql连接不上");

    private Integer code;

    private String message;
}
