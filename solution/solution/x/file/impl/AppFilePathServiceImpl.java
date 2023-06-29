//package com.example.solution.common.file.impl;
//
//
//import com.example.solution.common.file.AppFilePathService;
//import org.springframework.stereotype.Service;
//
///**
// * 应用程序目录服务类
// *
// *
// * @date 2021/12/25
// */
//@Service
//public class AppFilePathServiceImpl implements AppFilePathService {
//
//    /**
//     * 文件路径配置
//     */
//    private final AppFilePathConfig appFilePathConfig;
//
//    public AppFilePathServiceImpl(AppFilePathConfig appFilePathConfig) {
//        this.appFilePathConfig = appFilePathConfig;
//    }
//
//    @Override
//    public String combine(String path, String... more) {
//        return FileUtils.combine(path, more);
//    }
//
//    @Override
//    public String getDateDayPath() {
//
//        return DateUtils.parseDateToStr(DateUtils.DATE_FORMAT_DAY_UNDERSCORE, DateUtils.getNowDate());
//    }
//
//    @Override
//    public String appendDateDayPath(String path) {
//        if (null == path) {
//            return StrUtil.EMPTY;
//        }
//
//        return this.combine(path, this.getDateDayPath());
//    }
//
//    /**
//     * 拼接路径
//     *
//     * @param path           根路径
//     * @param appendDatePath 拼接日期路径
//     * @return 拼接完成路径
//     */
//    private String appendDateDayPath(String path, boolean appendDatePath) {
//        if (appendDatePath) {
//            path = this.appendDateDayPath(path);
//        }
//
//        return path;
//    }
//}
