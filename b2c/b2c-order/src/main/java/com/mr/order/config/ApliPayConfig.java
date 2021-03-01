package com.mr.order.config;

public class ApliPayConfig {

    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static final String APP_ID = "2016092800613459";
    //应用私钥
    public static final String MERCHANT_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCgGzzfRCoT9Lvr3NZ3srHlGzwpLROsioVEwCn8QEIEBdtaxClBFwY46SUz56Z8BOfJR0XyNON0zI0s1NcD3v3BDi8CpLAL4lAwm3xJWSFQFdLi2pnCpqbtTl+OoMPdY1Ex2Ppy3YJwX3woG4t6RDjLbQGlcwyC0pyMBcMd3+KnQVneVnMrzzDp770LI8ik25SJS2mjkFW8FWth8j8dZc2r035WDDYGdQh1kbjBw4CMN48AbrY9QPpXq7Zo0rDXhaSCeh5gpzPPuiMmP1LYYSEIoyh59O86l/QILuLs/qvbm6haXIjm96FS1y85lUZaCj5HFKKi08Lz9bghjXNwHm6ZAgMBAAECggEAF7Yw+7fyq2XxdusL8EpMwmuYK1j7svAI8LetACjcHhBn/jdTDMrGJyOQWPGT3IY1db/Sgh6HGUM6HUugVHhwCGl7WN7fG8N2sNw2Of6zSlMRGnvZjdR0oGydBliU6XLOEZ3a6awYp1/c2WvK6l60MpyAQ+qYDb/3Y11vGjuQK8jr5fO8Zcv/KFsFBCuUbydrgFbb2u/RTaLY3/R4YUUw/IPnFACTENzS2qnsI1L0gNY1coIVyDA8UoMoJbM3o256/152Xxp7tH6FTftkCqwX8zIScojZHfGTLdPnhL3A1dKr2ddS1axO9scuHHWYnzylLXvcpjkYsz6sC890yNS6IQKBgQDUtyUc4jYAphfvJeC5+wgIGaVVaUpltJBAspYn/wXL5Yoe7eL8x4VW+aXN11lL/OmK/W7Y3UkFsCesC2qMdfv1wYiKR7Yl67gsqaL9DVJLRzSjgQSD7i8aXkkI3WI7mMvbGY/Vw2yOeM/Wg9WC48UT+HtnoeEnquUTKvzQzhfVLQKBgQDAr5A6Bn0nE7rBTzkEx5Tqf05m5MXMpd/U/xVlMmEAnyy9W9OUrY7MIwQOb9iDDv7vQv+lZiPUuGjovW4e0zZ7sd8EXNy5TiLv/DJbpZnsMYbY1I9C3tPnqIzuWmDXWO1nUy2+WTanyp8j3PNgy770wvCHmIH1iHz17EqsA9a6nQKBgE0p+v6VWP23QutwFCujUvCNTRrEzUZD0CiIIEx2CMZEiGxb44HLZyV7/pfDTRNxtc7DzNhCveCgXGPe6FH34uTtxfhTt1HeRrt89jFSCc5hEElX3il12AR6u4fJt37jGOBsT28+Kz0rukmZHXUT50QtWaboZGMdbWgcqgWdHawpAoGALhVPDzGiGQh07+q8nlIFYZgU7jLkw68gO1bOjSdhX76x+o/NqI9hyZzOSY2iYKXjfuGtNqeZBjUckh1MCv9E1oyR4/85ou/3FyJgAN84Uu79azpbEQkt8Qe0vQTMiHRyQvX3yMCZe8PQD8m2Q343K168HBHzcv+zszVSHpXEc1kCgYA1UIqwubM0+1HO4dfaNlNYN14FQkAlSrE9uBqzg3SBgan0Mni3+uykkOIDFRmYlecWWM531iudKfnydIFF/mKkV4477t0pZToiXqTcxkqWNRahrzBLf9jDYqaKHz5BtSshGymdRDL1gO2rsXuw8VbEgGuvTBi+k7XPBfk4/TzyUg==";
    //支付宝公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAytxMOvQnHBh/xux0WMYAC3wsHJtZqfujXRC8PBJ9CFv4uFqNFiMUcxhuuWwHnTHFpGH6wXqVBGcfNDUv+UO8EvHP9Pohn+GKC+QHabS1VQrrLzqmmchkEZ0WscmFIe3mlKYO2yT6IyC/pZNISzis4QIihLzlUVJY76ea7dzgMt03MkNiqFn0pgviz73QDzKhQs9NxAt2O6ce/LPtHAuE7HfwExqa+n0q4PPwsny4ata2uF5d/Xi0byZKHAYdvX9uTdT1Ee+z9n2Gmo3teW2CIDnDn3+kc5Zh+ZJLvs8Re+toiYha23aacQpl/zfZSn+1Uhvwb0hw7NSRl83J71Qi1QIDAQAB";
    //通知的回调地址 写一个确实支付成功改订单状态等，//必须为公网地址，
    public static final String NOTIFY_URL = "http://127.0.0.1:8089/paySuccess";
    //用户支付完成回调地址//可以写内网地址
    public static final String RETURN_URL = "http://127.0.0.1:8089/callback";
    // 签名方式
    public static String SIGN_TYPE = "RSA2";
    // 字符编码格式
    public static String CHARSET = "gbk";
    // 支付宝网关
    public static String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

}