package com.example.solution.common.file;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.gigacloud.starcloud.common.exception.ServiceException;
import com.gigacloud.starcloud.common.utils.MessageUtils;
import com.gigacloud.starcloud.common.utils.poi.ExcelUtil;
import com.gigacloud.starcloud.common.utils.spring.SpringUtils;
import com.gigacloud.starcloud.pojo.common.domain.CommonImportBatch;
import com.gigacloud.starcloud.pojo.common.dto.CommonImportContext;
import com.gigacloud.starcloud.pojo.common.dto.CommonImportFileConfig;
import com.gigacloud.starcloud.pojo.core.constant.CommonImportFileConstants;
import com.gigacloud.starcloud.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 通用文件导入业务类
 *
 * 
 * @date 2022/3/28 9:58
 */
@Slf4j
public abstract class AbstractImportFileService<T> {
    /**
     * 导入配置
     */
    protected final CommonImportFileConfig filePathConfig;
    /**
     * 线程隔离对象-上下文
     */
    private final ThreadLocal<CommonImportContext<T>> threadLocalContext = new ThreadLocal<>();

    public AbstractImportFileService(String importType) {
        ISysConfigService sysConfigService = SpringUtils.getBean(ISysConfigService.class);
        try {
            String configByKey = sysConfigService.selectConfigByKey(CommonImportFileConstants.CONFIG_PREFIX + importType + CommonImportFileConstants.CONFIG_SUFFIX);
            CommonImportFileConfig config = JSONUtil.toBean(configByKey, CommonImportFileConfig.class);
            if (config == null) {
                log.error(">>>>>>>>>>导入类型：{}【initFilePath】>>>>>>>>>>文件路径初始化失败", importType);
                throw new ServiceException("Failed to initialize the save file path");
            }
            this.filePathConfig = config;
        } catch (Exception e) {
            log.error(">>>>>>>>>>导入类型：{}【initFilePath】>>>>>>>>>>文件路径初始化出现异常", importType, e);
            throw new ServiceException("File path initialization is abnormal");
        }

    }

    /**
     * 获取当前导入工具类
     * @return 导入工具类
     */
    protected ExcelUtil<T> getCurrentUtil() {
        return getCurrentContext().getExcelUtil();
    }

    /**
     * 获取当前导入工具类
     * @return 导入工具类
     */
    protected List<T> getCurrentSource() {
        return getCurrentContext().getSourceList();
    }

    /**
     * 获取导入上下文
     * @return 导入上下文
     */
    protected CommonImportContext<T> getCurrentContext() {
        CommonImportContext<T> context = threadLocalContext.get();
        if (context == null) {
            throw new ServiceException("Empty import file context");
        }
        return context;
    }

    /**
     * 初始化当前线程上下文
     */
    void initThreadLocalContext() {
        threadLocalContext.set(prepareImportContext());
    }

    /**
     * 释放thread local参数
     */
    void clearThreadLocalContext() {
        threadLocalContext.remove();
    }

    /**
     * 获取模板文件路径
     * @return
     */
    String findTemplateFilePath() {
        // 根据不同的环境提供不同模板
        if (MessageUtils.isZhCnLocale()) {
            return filePathConfig.getZhTemplateFile();
        } else {
            return filePathConfig.getEnTemplateFile();
        }
    }

    /**
     * 获取文件保存路径
     * @return
     */
    String findSourceFilePath() {
        return filePathConfig.getSourceFile();
    }

    /**
     * 导入文件模板校验
     * @param file 文件
     * @return 校验结果
     * @throws Exception 异常信息
     */
     boolean checkExcelTemplate(MultipartFile file) throws Exception {
         return getCurrentUtil().checkExcelTemplate(file.getInputStream(), ObjectUtil.defaultIfNull(filePathConfig.getTitleIndex(), 1));
     }

    /**
     * 解析文件并加载至上下文
     * @param file 文件
     * @throws Exception 异常信息
     */
    void parseFileThenLoadContext(MultipartFile file) throws Exception {
        List<T> sourceList = getCurrentUtil().importExcel(file.getInputStream(), ObjectUtil.defaultIfNull(filePathConfig.getTitleNum(), 2),
                ObjectUtil.defaultIfNull(filePathConfig.getTitleIndex(), 1), filePathConfig.getMaxRows());
        getCurrentContext().setSourceList(sourceList);
    }

    /**
     * 准备导入上下文
     * @return 导入上下文
     */
    abstract protected CommonImportContext<T> prepareImportContext();

    /**
     * 校验导入文件，并生成错误文件
     * @param file 文件对象
     * @return 错误文件路径
     * @throws Exception 异常信息
     */
    abstract protected String validateFile(MultipartFile file) throws Exception;

    /**
     * 保存校验通过的数据
     * @param importBatch 导入批次信息
     * @return 保存结果
     */
    abstract protected boolean saveValidData(CommonImportBatch importBatch);

}
