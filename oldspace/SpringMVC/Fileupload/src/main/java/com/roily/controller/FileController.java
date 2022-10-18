package com.roily.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/1/10
 */
@Controller
public class FileController {

    @ResponseBody
    @RequestMapping("/t")
    public String t1() {
        return "大萨达";
    }

    //@RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
    //批量上传CommonsMultipartFile则为数组即可
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        //获取文件名 : file.getOriginalFilename();
        String uploadFileName = file.getOriginalFilename();
        System.out.println(uploadFileName);

        //获取文件的大小
        long size = file.getSize();
        System.out.println(size);

        //获取网络资源类型  不用以后缀的方式判断资源类型 .jpg.... image/png
        String contentType = file.getContentType();
        System.out.println(contentType);


        //如果文件名为空，直接回到首页！
        if ("".equals(uploadFileName)) {
            return "文件上传失败";
        }
        System.out.println("上传文件名 : " + uploadFileName);

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        //如果路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址：" + realPath);

        InputStream is = file.getInputStream(); //文件输入流
        OutputStream os = new FileOutputStream(new File(realPath, uploadFileName)); //文件输出流

        //读取写出
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            os.flush();
        }
        os.close();
        is.close();
        return "文件上传成功";
    }


    /*
     * 采用file.Transto 来保存上传的文件
     */
    @RequestMapping("/upload2")
    public String fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        //上传文件地址
        System.out.println("上传文件保存地址：" + realPath);

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath + "/" + file.getOriginalFilename()));

        return "redirect:/index.jsp";
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public String downloads(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //要下载的图片地址
        String path = request.getServletContext().getRealPath("/upload");
        String fileName = request.getParameter("fileName");

        System.out.println(fileName);
        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头 解决弹出框中文乱码问题
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path, fileName.trim());
        if (!file.exists()) {
            return "资源不存在";
        }
        //2、 读取文件--输入流
        InputStream input = new FileInputStream(file);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff = new byte[1024];
        int index = 0;
        //4、执行 写出操作
        while ((index = input.read(buff)) != -1) {
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }
}
