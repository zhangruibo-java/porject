package com.mr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class TestController {
    /**
     * 模拟登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @GetMapping("login")
    public String testLogin(String username,String password,HttpSession session){
        System.out.println("测试登陆方法");
        if(username.equals("tomcat") && password.equals("111111")){
            System.out.println("登录成功，创建session");
            session.setAttribute("admin",username);
            return "login success";
        }else{
            return "username or password is error";
        }
    }

    /**
     * 获得session
     * @param session
     * @return
     */
    @GetMapping("show")
    public String testsess(HttpSession session){
        System.out.println("获得session方法"+session.getId());
           Object obj= session.getAttribute("admin");
           if(obj == null){
               return "no login";
           }else{
                return"login user is" + obj.toString();
           }

    }
}
