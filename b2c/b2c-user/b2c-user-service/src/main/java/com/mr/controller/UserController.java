package com.mr.controller;

import com.mr.pojo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /*GET /check/{data}/{type}*/

    /**
     * 校验数据账号是否可用
     * @param data 数据内容
     * @param type 1 用户名 2 手机号
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(
            @PathVariable("data") String data,
            @PathVariable("type") Integer type){
        //返回布尔类型结果：- true：可以使用，用户表不存在 - false：不可用，用户表已注册
        //type: 1 用户名 2 手机号
        //data: 数据库数据
        if (data == null || type == null || type<=0 || type >2){
            return new ResponseEntity<>(HttpStatus. BAD_REQUEST);//- 400：参数有误
        }
        Boolean resp=null;
        try{
            resp= userService.validNameAndPhone(data,type);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus. INTERNAL_SERVER_ERROR);
        }

        //状态码：- 200：校验成功- 400：参数有误- 500：服务器内部异常
        return  ResponseEntity.ok(resp);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void>  register(User user){
        boolean result = userService.register(user);
        //注册失败，返回400请求
        if(!result){
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping("query")
    public ResponseEntity<User> query(
            @RequestParam("username")String username,
            @RequestParam("password")String password
                                      ){
        User user = null;
        try{
            user =userService.query(username,password);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

       if(user == null){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
        return  ResponseEntity.ok(user);

    }
}
