package com.mr.api;

import com.mr.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    /*GET /check/{data}/{type}*/

    /**
     * 校验数据账号是否可用
     * @param data 数据内容
     * @param type 1 用户名 2 手机号
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public Boolean check(
            @PathVariable("data") String data,
            @PathVariable("type") Integer type);

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("register")
    public void  register(User user);
    @GetMapping("query")
    public User query(
            @RequestParam("username")String username,
            @RequestParam("password")String password
    );
}
