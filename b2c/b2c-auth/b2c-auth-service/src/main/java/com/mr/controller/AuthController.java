package com.mr.controller;

import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.service.AuthService;
import com.mr.util.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mr.common.utils.CookieUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response
                                      ){
        //校验账号密码是否正确
        String token = authService.auth(username,password);
        //如果token证明账号密码错误，返回401无权限
        if (StringUtils.isEmpty(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //如果token有值 就放入cookie中
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token,jwtConfig.getCookieMaxAge(),true);
        return ResponseEntity.ok(null);
    }
    /**
     * 解析token数据 http://api.b2c.com/api/auth/verify
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("B2C_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        System.out.println("----"+token);
        try{
            UserInfo userInfo = JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
            String newToken = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),newToken,jwtConfig.getCookieMaxAge(),true);
            return ResponseEntity.ok(userInfo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }


    }
}
