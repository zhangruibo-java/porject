package com.mr.util;

import com.mr.util.JwtUtils;
import com.mr.util.RsaUtils;
import com.mr.bo.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTokenTest {

    //公钥位置
    private static final String pubKeyPath = "C:\\Users\\mac\\JavaFile\\zrb-source\\resKey\\rea.pub";
    //私钥位置
    private static final String priKeyPath = "C:\\Users\\mac\\JavaFile\\zrb-source\\resKey\\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "9527");
    }


    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(10l, "tomcat"), privateKey, 20);
        System.out.println("user-token = " + token);
    }


    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
String token ="eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoidG9tY2F0IiwiZXhwIjoxNjEyNDg4NTc3fQ.qjQ9wx8-PvBAqyhrXS9huhMxFwJV31TODCni-iFhMrqg-37wVOY1GooTKbhKfPqy7hGl7wBNDepoeF60gYrPvNqBExrhs2GqcTBKgGt1fka8y-IKD39i21xba_de9oze-kKJtb2zCWH6n25ohDchLLpVRt7VS7oBAg83QTD5YJM";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}


