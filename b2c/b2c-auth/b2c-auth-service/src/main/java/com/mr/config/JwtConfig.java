package com.mr.config;

import com.mr.util.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Configuration
@PropertySource("classpath:application.yml")
public class JwtConfig {
    @Value("${b2c.jwt.secret}")
    private String secret; // 密钥
    @Value("${b2c.jwt.pubKeyPath}")
    private String pubKeyPath;// 公钥
    @Value("${b2c.jwt.priKeyPath}")
    private String priKeyPath;// 私钥
    @Value("${b2c.jwt.expire}")
    private int expire;// token过期时间
    @Value("${b2c.jwt.cookieName}")
    private String cookieName;// cookie名称
    @Value("${b2c.jwt.cookieMaxAge}")
    private int cookieMaxAge;// cookie名称


    private PublicKey publicKey; // 公钥

    private PrivateKey privateKey; // 私钥

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

}