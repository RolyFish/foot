package com.example.solution.common.file;

import java.io.File;
import java.util.List;

/**
 * 文件服务
 *
 * 
 * @date 2021/12/25
 */
public interface AppFileService {

    /**
     * 设置根目录
     *
     * @param rootPath 设置根目录
     */
    void setRootPath(String rootPath);

    /**
     * 设置根目录
     *
     * @return 根据不同配置返回不同根目录
     */
    String getRootPath();

    /**
     * 创建目录
     *
     * @param filePath 文件路径
     * @return true：创建成功，false：失败
     */
    Boolean mkdirs(String filePath);

    /**
     * 依据完整文件路径创建文件
     *
     * @param filePath 创建文件
     * @return 自动创建文件目录，并创建文件
     */
    File touch(String filePath);

    /**
     * 列出目录和文件
     *
     * @param filePath 文件路径
     * @return 目录和文件列表
     */
    List<File> ls(String filePath);

    /**
     * 依据完整文件路径查找文件
     *
     * @param filePath 创建文件
     * @return 自动创建文件目录，并创建文件
     */
    File find(String filePath);

    /**
     * 当前文件系统模式下完整路径
     *
     * @param filePath 原始路径
     * @return 当前文件系统模式下完整路径
     */
    String findPath(String filePath);

}
