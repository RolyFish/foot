package com.example.solution.common.file.impl;


import com.example.solution.common.file.AppFileService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地磁盘默认存储实现
 *
 * 
 * @date 2021/12/25
 */
@Data
@Service
public class AppFileDefaultStorageServiceImpl implements AppFileService {

    /**
     * 默认根目录
     */
    private String rootPath = "D:/"/*AppFilePathConfig.APP_FILE_DEFAULT_STORAGE_ROOT_PATH*/;

    /**
     * 生成完整路径
     *
     * @param filePath 文件路径
     * @return 完整路径
     */
    private String getFullPath(String filePath) {
        return FileUtils.combine(this.getRootPath(), filePath);
    }

    @Override
    public Boolean mkdirs(String filePath) {
        return FileUtils.createDirectory(this.getFullPath(filePath));
    }

    @Override
    public File touch(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            throw new IORuntimeException("file full name not exists error");
        }

        return FileUtil.touch(this.getFullPath(filePath));
    }

    @Override
    public List<File> ls(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return new ArrayList<>();
        }

        File[] fileList = FileUtil.ls(this.getFullPath(filePath));
        if (null == fileList) {
            return new ArrayList<>();
        }

        return Arrays.stream(fileList).collect(Collectors.toList());
    }

    @Override
    public File find(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            throw new IORuntimeException("file full name not exists error");
        }

        return new File(this.getFullPath(filePath));
    }

    @Override
    public String findPath(String filePath) {
        String ret = StrUtil.EMPTY;
        if (null == filePath) {
            return ret;
        }

        ret = this.getFullPath(filePath);
        return ret;
    }

}
