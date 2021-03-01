package com.mr.config;

import com.mr.util.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@Configuration
@PropertySource("classpath:application.yml")
public class JwtConfig {

    @Value("${b2c.jwt.pubKeyPath}")
    private String pubKeyPath;// 公钥

    private PublicKey publicKey; // 公钥

    @Value("${b2c.jwt.cookieName}")
    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }
}