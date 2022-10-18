package com.roily.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RoilyFish
 */
public class HelloController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg","mvc再回顾");
        mv.setViewName("hello");
        httpServletRequest.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(httpServletRequest,httpServletResponse);
        //不能用重定向
        //httpServletResponse.sendRedirect("/index.jsp");

        return mv;

    }

}
