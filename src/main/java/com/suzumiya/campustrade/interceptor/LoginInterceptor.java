package com.suzumiya.campustrade.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //取session
        HttpSession session = request.getSession(false);//false 表示只在已有 Session 时获取，不新创建
        if (session != null){
            //取userId
            Long userId = (Long)session.getAttribute("userId");
            //注入请求属性
            request.setAttribute("userId", userId);
        }
        //即便没有 userId（比如未登录访问），也先放行，让具体的 Controller 自行决定是否需要登录
        return true;
    }
}
