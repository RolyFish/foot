package com.roily.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/10
 */
@Controller
@RequestMapping("/req")
public class ReqResp {
    /**
     *  原生的重定向方式，除了restful风格外，请求参数都在req域中
     *  但是实现不了参数的传递
     *
     *  原因：测试发现，即便执行转发接下来的代码也会执行，req域失效（但是手动set又可以传递值）
     */

    @RequestMapping("/t1/{msg}")
    public String t1(@PathVariable("msg") String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException {
        String msg1 = req.getParameter("msg");
        //restful风格参数不在req域中
        System.out.println(msg1);

        System.out.println(msg);

        req.getRequestDispatcher("/WEB-INF/req/req.jsp").forward(req, resp);

        System.out.println("continue");

        return "from";
    }

    @RequestMapping("/t2")
    public String t2(HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg = req.getParameter("msg");

        System.out.println(msg);
        //无法传递req域中的数据
        //手动set可以传值
        req.setAttribute("msg", "手动set");

        req.getRequestDispatcher("/WEB-INF/req/req.jsp").forward(req, resp);

        System.out.println("continue");

        return "from";
    }
    @RequestMapping("/t3")
    public String t3(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        //无法传递req域中的数据
        //手动set可以传值
        //req.setAttribute("msg", "手动set");

        req.getRequestDispatcher("/WEB-INF/req/req.jsp").forward(req, resp);

        System.out.println("continue");

        return "from";
    }


    /**
     * 不行得走视图解析器
     * @param msg
     * @param resp
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping("/t4")
    public String t4(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        return "/WEB-INF/req/req.jsp";
    }

    @RequestMapping("/t5")
    public String t5(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        req.setAttribute("msg","set传值");

        return "forward:/WEB-INF/req/req.jsp";
    }

    @RequestMapping("/t6")
    public String t6(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        return "redirect:/WEB-INF/req/req.jsp";
    }

    @RequestMapping("/t7")
    public String t7(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        return "redirect:/index.jsp";
    }

    @RequestMapping("/r")
    public String r() {

        System.out.println("走请求转发");
        return "resp";
    }

    @RequestMapping("/t8")
    public String t8(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        return "redirect:/req/r";
    }

    @RequestMapping("/t9")
    public String t9(String msg, HttpServletResponse resp, HttpServletRequest req) throws ServletException, IOException, InterruptedException {

        String msg1 = req.getParameter("msg");

        System.out.println(msg1);

        System.out.println(msg);

        //req.setAttribute("msg","set传值");

        return "forward:/req/r";
    }
}
