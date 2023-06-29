package com.example.solution.common.file.impl;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * NAS 网络磁盘访问
 *
 * 
 * @date 2021/12/25
 */
@Service
public class AppFileNasStorageServiceImpl extends AppFileDefaultStorageServiceImpl {

    @Override
    public void setRootPath(String rootPath) {
        super.setRootPath(rootPath);
    }

    @Override
    public String getRootPath() {
        return super.getRootPath();
    }

    @Override
    public Boolean mkdirs(String filePath) {
        return super.mkdirs(filePath);
    }

    @Override
    public File touch(String filePath) {
        return super.touch(filePath);
    }

    @Override
    public List<File> ls(String filePath) {
        return super.ls(filePath);
    }

    @Override
    public File find(String filePath) {
        return super.find(filePath);
    }

    @Override
    public String findPath(String filePath) {
        return super.findPath(filePath);
    }
}
