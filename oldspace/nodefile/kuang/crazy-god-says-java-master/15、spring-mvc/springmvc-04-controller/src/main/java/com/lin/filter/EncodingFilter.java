package com.lin.filter;


import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest Request, ServletResponse Response, FilterChain filterChain) throws IOException, ServletException {
        Request.setCharacterEncoding("utf-8");
        Response.setCharacterEncoding("utf-8");
        filterChain.doFilter(Request,Response);
    }

    public void destroy() {

    }
}
