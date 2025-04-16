package com.cqu.filmsystem.intercepter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查用户是否登录，比如检查 session 中是否存在用户信息
        Object loginUser = request.getSession().getAttribute("user");


        // 获取请求的URL
        String url = request.getRequestURL().toString();

        // 如果需要查询字符串，可以使用 request.getQueryString()
        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }

        if (loginUser == null) {
            // 用户未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/user/login.html");
            return false; // 返回 false 表示请求结束，不再调用其他的拦截器或处理器
        }

        return true; // 用户已登录，请求将正常继续执行
    }


}
