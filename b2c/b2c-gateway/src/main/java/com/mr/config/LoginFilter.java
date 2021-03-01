package com.mr.config;

import com.mr.common.utils.CookieUtils;
import com.mr.common.utils.JsonUtils;
import com.mr.util.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component//实例化
//@EnableConfigurationProperties()//指定注入
public class LoginFilter extends ZuulFilter {
    /**
     * 过滤器的类型 pre post....
     * @return
     */
    @Override
    public String filterType() {
        //进入方法之前执行
        return "pre";
    }

    /**
     *过滤器的优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 5;
    }

    /**
     * 过滤器是否生效  false 不生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
//如果该请求是在白名单类不用拦截 过滤器不生效

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url=request.getRequestURI();
        System.out.println("请求路径 ："+url);


        return !isAllowPath(url);
    }

    /**
     * 判断请求是否在白名单
     * @param requestUrl
     * @return
     */
    public boolean isAllowPath(String requestUrl){
    //默认不存在于白名单
        boolean result = false;
        //如果当前路径在白名单内 放行
        for(String  path : filterProperties.getAllowPaths()){
            if(requestUrl.startsWith(path)){//存在白名单中
                result = true;
                //跳出循环
                break;
            }
        }
    return  result;
    }

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private FilterProperties filterProperties;
    /**
     * 运行方法
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("白名单那"+filterProperties.getAllowPaths());
        /**
         * 过滤器的作用 是 拦截非登录请求
         * 1 获取token 从cookie中获取
         * 2 通过公钥 解析 token
         * 3 解析失败 请求拦截
         * 4 登录设置白名单（auth user search item 等等）
         * RequestContext ctx = RequestContext.getCurrentContext();
         *         HttpServletRequest request = ctx.getRequest();
         */
        //获取token 从cookie中获取
        //获取request
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String token= CookieUtils.getCookieValue(request,jwtConfig.getCookieName());
        System.out.println("输出token "+token);
        System.out.println(jwtConfig.getCookieName());
        // 2 通过公钥 解析 token
        try {
            JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //解析成功
            System.out.println("已登录");


        }catch (Exception e){
            //解析失败
            System.out.println("未登录 解析失败");
            //设置不允许继续执行后 登录方法也被拦截了
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            //登录拦截器已完成
            //登录方法不能被拦截（放过登录请求）
            //登录拦截器 需要放过一些请求
            // 登录
            // 搜索
            //加入白名单

        }
        return null;
    }
}
