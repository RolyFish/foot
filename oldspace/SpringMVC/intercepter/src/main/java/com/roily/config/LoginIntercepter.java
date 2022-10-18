package com.roily.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginIntercepter implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginfo = request.getSession().getAttribute("loginfo");
        if(loginfo!=null){
            return true;
        }if (request.getRequestURI().contains("login")){
            return true;
        }if (request.getRequestURI().contains("tologin")){
            return true;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/tologin.jsp").forward(request,response);
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
