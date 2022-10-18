package com.roily.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RoilyFish
 */
public class servlestReview extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("add".equals(method)){
            System.out.println("add方法");
            req.getSession().setAttribute("msg","执行了add方法");
        }else if("delete".equals(method)){
            System.out.println("delete方法");
            req.getSession().setAttribute("msg","执行了delete方法");
        }
        req.getRequestDispatcher("WEB-INF/jsp/result.jsp").forward(req,resp);
        //resp.sendRedirect("/WEB-INF/jsp/result.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.doGet(req,resp);
    }
}
