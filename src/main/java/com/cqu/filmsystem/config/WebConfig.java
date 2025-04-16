package com.cqu.filmsystem.config;

import com.cqu.filmsystem.intercepter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并指定拦截路径和排除路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 指定拦截器拦截的路径，这里表示拦截所有请求
                .excludePathPatterns(  // 指定拦截器不拦截的路径
                        "/user/login.html",       // 登录页面
                        "/user/login",     // 登录处理请求
                        "/user/register.html",     // 注册页面
                        "/user/register",     // 注册请求处理
                        "/movice/**",     // 电影推荐页
                        "/movice/details",     // 电影推荐页
                        "/type/showAll",     // 电影分类显示
                        "/static/css/**",      // 静态资源
                        "/static/js/**",
                        "/static/images/**");
    }

}
