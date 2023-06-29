package com.example.solution.common.file;

/**
 * 文件服务路径
 *
 * 
 * @date 2021/12/25
 */
public interface AppFilePathService {

    /**
     * 拼接路径
     *
     * @param path 根路径
     * @param more 更多路径
     * @return 拼接完成路径
     */
    String combine(String path, String... more);

    /**
     * 获取日期目录路径
     *
     * @return 日期目录路径
     */
    String getDateDayPath();

    /**
     * path 后面添加年月日的目录
     *
     * @param path 年月日目录
     * @return 添加了年月日的新目录
     */
    String appendDateDayPath(String path);

}
