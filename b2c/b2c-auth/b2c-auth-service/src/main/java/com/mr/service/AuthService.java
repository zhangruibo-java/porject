package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.clent.UserClient;
import com.mr.config.JwtConfig;
import com.mr.pojo.User;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserClient userClient;

    /**
     * 校验账号密码，如果正确，组装token返回
     * @param username
     * @param password
     * @return
     */
    public String auth(String username, String password) {
        try {
            User user = userClient.query(username, password);
            if (user == null) {//账号密码不对
                return null;
            }
            //账号密码正确
            // 生成token
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());
            String token = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
            return token;
        }catch (Exception e){
            return null;
        }

    }
}
