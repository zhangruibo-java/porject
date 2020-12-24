package com.mr.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
   /* @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerException(RuntimeException e){
        //规定返回的状态码?? ,然后异常的提示信息设置到body中
        System.out.println("-------异常----------");
        //异常自己规定 不能写死
        //RuntimeException 不支持写状态码 所以要自定义异常
        return ResponseEntity.status(400).body(e.getMessage());

    }*/
 /*  @ExceptionHandler(MrException.class)
   public ResponseEntity<String> handlerException(MrException e){
       //规定返回的状态码?? ,然后异常的提示信息设置到body中
       System.out.println("-------异常----------");
       //异常自己规定 不能写死
       //RuntimeException 不支持写状态码 所以要自定义异常
      // new MrException(e.getCode(),e.getMessage());
       //通过枚举取值

       return ResponseEntity.status(e.getExceptionEnums().getCode() ).body(e.getExceptionEnums().getMessage());

   }*/
   @ExceptionHandler(MrException.class)
   public ResponseEntity<ExceptionResult> handlerException(MrException e){
       //规定返回的状态码?? ,然后异常的提示信息设置到body中
       System.out.println("-------异常----------");
       //异常自己规定 不能写死
       //RuntimeException 不支持写状态码 所以要自定义异常
       // new MrException(e.getCode(),e.getMessage());
       //通过枚举取值

       return ResponseEntity.status(e.getExceptionEnums().getCode() ).body(new ExceptionResult(e.getExceptionEnums().getCode(),e.getExceptionEnums().getMessage()));

   }
}
