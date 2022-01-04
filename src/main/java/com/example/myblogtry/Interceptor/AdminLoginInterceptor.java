package com.example.myblogtry.Interceptor;


import org.aopalliance.intercept.Interceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过session中是否含有登录者信息来进行验证判断
        String requestServletPath = request.getServletPath();
        if( requestServletPath.startsWith("/admin") && null==request.getSession().getAttribute("loginUser")){
            //重新跳转到登陆页面，并显示信息
                //在session中存放信息
            request.getSession().setAttribute("errorMsg","请重新登录");
                //重定向到页面
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }else{
            //直接放过请求，继续执行
                //考虑之前可能存在的未登录信息，需要更新信息
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
