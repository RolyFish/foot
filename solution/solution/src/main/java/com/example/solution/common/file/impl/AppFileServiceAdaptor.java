package com.example.solution.common.file.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gigacloud.starcloud.common.config.AppFilePathConfig;
import com.gigacloud.starcloud.common.config.AppFilePathServiceConfig;
import com.gigacloud.starcloud.common.config.AppFilePathServiceProfileConfig;
import com.gigacloud.starcloud.common.constant.AppFilePathConstants;
import com.gigacloud.starcloud.common.exception.custom.AppFilePathServiceNotExistsException;
import com.gigacloud.starcloud.common.file.AppFileService;
import com.gigacloud.starcloud.common.utils.FastJsonUtil;
import com.gigacloud.starcloud.pojo.core.constant.SysParamConfigConstants;
import com.gigacloud.starcloud.system.service.ISysConfigService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件存储服务适配器
 *
 * 
 * @date 2021/12/25
 */
@Service
public class AppFileServiceAdaptor implements AppFileService {

    /**
     * application 文件保存服务类
     */
    private final Map<String, AppFileService> appFileServiceMap;

    /**
     * 统一配置管理
     */
    private final ISysConfigService sysParamConfigService;

    public AppFileServiceAdaptor(Map<String, AppFileService> appFileServiceMap,
                                 ISysConfigService sysParamConfigService) {
        this.appFileServiceMap = appFileServiceMap;
        this.sysParamConfigService = sysParamConfigService;
    }

    /**
     * 取的当前环境的 AppFileService，若无匹配则采用本地服务“AppFileLocalStorageServiceImpl”
     *
     * @return 具体文件服务实例
     */
    private AppFileService findEnvAppFileService() {
        AppFilePathServiceProfileConfig paramConfig = this.findEnvFileService();
        if (null == paramConfig
                || StrUtil.isBlank(paramConfig.getServiceName())
                || StrUtil.isBlank(paramConfig.getRootPath())) {
            throw new AppFilePathServiceNotExistsException(StrUtil.EMPTY);
        }
        AppFileService ret = this.appFileServiceMap.get(paramConfig.getServiceName());
        if (null == ret) {
            throw new AppFilePathServiceNotExistsException(paramConfig.getServiceName());
        }

        // 设置根目录
        if (StrUtil.isNotBlank(paramConfig.getRootPath())) {
            ret.setRootPath(paramConfig.getRootPath());
        }

        return ret;
    }

    /**
     * 获取当前环境下的应用程序文件服务
     *
     * @return 应用程序文件服务实例
     */
    private AppFilePathServiceProfileConfig findEnvFileService() {
        AppFilePathServiceProfileConfig ret = new AppFilePathServiceProfileConfig();
        ret.setRootPath(AppFilePathConfig.APP_FILE_DEFAULT_STORAGE_ROOT_PATH);
        ret.setServiceName(AppFilePathConfig.APP_FILE_LOCAL_STORAGE_SERVICE_NAME);

        String configByKey = this.sysParamConfigService.selectConfigByKey(SysParamConfigConstants.LOGISTICS_APP_FILE_SERVICE_STRATEGY);
        if (null == configByKey) {
            return ret;
        }

        try {
            AppFilePathServiceConfig configDO = FastJsonUtil.jsonToBean(configByKey,
                    AppFilePathServiceConfig.class);
            if (StrUtil.isBlank(configDO.getActiveService())
                    || CollectionUtil.isEmpty(configDO.getServiceList())) {
                return ret;
            }

            for (AppFilePathServiceProfileConfig p : configDO.getServiceList()) {
                if (configDO.getActiveService().equalsIgnoreCase(p.getServiceName())) {
                    return p;
                }
            }
        } catch (Exception e) {
            Map<String, String> itemsMap = new LinkedHashMap<>();
            itemsMap.put(SysParamConfigConstants.LOGISTICS_APP_FILE_SERVICE_STRATEGY, configByKey);

        }

        return ret;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.findEnvAppFileService().setRootPath(rootPath);
    }

    @Override
    public String getRootPath() {
        return this.findEnvAppFileService().getRootPath();
    }

    @Override
    public Boolean mkdirs(String filePath) {
        return this.findEnvAppFileService().mkdirs(filePath);
    }

    @Override
    public File touch(String filePath) {
        return this.findEnvAppFileService().touch(filePath);
    }

    @Override
    public List<File> ls(String filePath) {
        return this.findEnvAppFileService().ls(filePath);
    }

    @Override
    public File find(String filePath) {
        return this.findEnvAppFileService().find(filePath);
    }

    @Override
    public String findPath(String filePath) {
        return this.findEnvAppFileService().findPath(filePath);
    }
    /**
     * 生成文件实际储存路径
     *
     * @param fileName 二维码图片名
     * @return 实际存储在linux的路径
     */
    public String generateActuallyTempPath(String fileName) {
        String tempFilePath = AppFilePathConstants.TEMP_FOLDER + fileName;
        // 获取实际存储路径
        String actualTempFilePath = this.findPath(tempFilePath);
        // 创建文件目录
        this.touch(tempFilePath);
        return actualTempFilePath;
    }

}
