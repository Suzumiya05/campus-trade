package com.suzumiya.campustrade.config;

import com.suzumiya.campustrade.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用参数 InterceptorRegistry 来注册LoginInterceptor
        // 链式调用 .addPathPatterns("/**") 拦截所有请求,接着调用 .excludePathPatterns(...) 排除掉不需要登录就能访问的路径
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/users/login").
        excludePathPatterns("/users/register").excludePathPatterns("/static/**").excludePathPatterns("/products");
        // /users/login(登录页面本身就不需要登录)、/users/register(注册页面本身不需要登录)
        // /products(商品浏览不需要登录)、/static/**()
    }
}
