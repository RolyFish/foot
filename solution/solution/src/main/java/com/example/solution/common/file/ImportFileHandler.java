package com.example.solution.common.file;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用文件导入处理类
 *
 * 
 * @date 2022/3/28 10:09
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportFileHandler {

    private final Map<String, AbstractImportFileService<?>> importFileServiceMap;
    private final ImportFileUtil importFileUtil;
    private final ICommonImportBatchService importBatchService;
    private final AliyunOssUtil aliyunOssUtil;

    public AbstractImportFileService<?> getImportFileService(String importType) {
        return importFileServiceMap.get(importType);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AjaxResult handleImportFile(String importType, MultipartFile file) throws Exception {
        StopWatch watch = new StopWatch("ImportFileHandler");
        log.info(">>>>>>>>>>【handleImportFile】>>>>>>>>>>导入文件开始,导入类型：{}", importType);
        // 获取登录用户信息
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        // 获取登录用户绑定的客户号
        AbstractImportFileService<?> abstractImportFileService = this.getImportFileService(importType);
        try {
            // 初始化线程上下文变量
            abstractImportFileService.initThreadLocalContext();
            // 校验导入文件表头信息
            if (StrUtil.isEmpty(file.getOriginalFilename()) || !abstractImportFileService.checkExcelTemplate(file)) {
                throw new ServiceException(MessageUtils.message("import.file.invalid.template"));
            }
            watch.start("ImportFileHandler#解析导入文件");
            // 解析文件成导入对象
            abstractImportFileService.parseFileThenLoadContext(file);
            watch.stop();
            watch.start("ImportFileHandler#文件上传阿里云");
            // 获取源文件保存路径
            String sourceFilePath = abstractImportFileService.findSourceFilePath();
            // 保存文件到阿里云OSS
            String fileFullPath = aliyunOssUtil.uploadFile(file, sourceFilePath, true);
            if (StrUtil.isEmpty(fileFullPath)) {
                log.error(">>>>>>>>>>【handleImportFile】>>>>>>>>>>文件保存失败,文件路径:{},文件名:{}", sourceFilePath, file.getOriginalFilename());
                throw new ServiceException("Failed to save the import file");
            }
            watch.stop();
            watch.start("ImportFileHandler#校验导入文件");
            // 对象校验
            String errorFilePath = abstractImportFileService.validateFile(file);
            // 初始化导入批次
            CommonImportBatch importBatch = initImportBatch(importType, fileFullPath, errorFilePath, loginUser);
            Long importBatchId = importBatchService.createImportBatch(importBatch);
            if (importBatchId == null) {
                log.error(">>>>>>>>>>【handleImportFile】>>>>>>>>>>导入批次保存失败,批次信息:{}", JSONUtil.toJsonStr(importBatch));
                throw new ServiceException("Failed to save the import batch");
            }
            watch.stop();
            // 保存校验通过后的批次数据
            if (StrUtil.isEmpty(errorFilePath)) {
                watch.start("ImportFileHandler#保存文件数据");
                boolean result = abstractImportFileService.saveValidData(importBatch);
                if (!result) {
                    log.error(">>>>>>>>>>【handleImportFile】>>>>>>>>>>批次明细保存失败,文件路径:{}", fileFullPath);
                    String failureMessage = importBatch.getFailureMessage();
                    throw new ServiceException("Failed to save the import file details!" + StrUtil.nullToDefault(failureMessage, StrUtil.EMPTY));
                }
                watch.stop();
            }
            log.info(">>>>>>>>>>【handleImportFile】>>>>>>>>>>导入文件结束,导入类型：{}", importType);
            log.info(">>>>>>>>>>【handleImportFile】>>>>>>>>>>导入文件耗时明细\r\n{}", watch.prettyPrint(TimeUnit.MILLISECONDS));
            return AjaxResult.success(StrUtil.isEmpty(errorFilePath) ? Constants.SUCCESS_MSG : Constants.FAILURE_MSG);
        } finally {
            // 线程变量删除，防止内存泄露
            abstractImportFileService.clearThreadLocalContext();
        }
    }

    private CommonImportBatch initImportBatch(String importType,
                                              String importFilePath,
                                              String errorFilePath,
                                              SysUser sysUser) {
        CommonImportBatch importBatch = new CommonImportBatch();
        String importBatchNumber = importFileUtil.generateImportBatchNumber();
        importBatch.setImportBatchNo(importBatchNumber);
        importBatch.setImportType(importType);
        importBatch.setImportFilePath(importFilePath);
        importBatch.setImportStatus(StrUtil.isEmpty(errorFilePath) ?
                CommonImportBatchStatusEnum.IMPORT_SUCCESSFUL.getCode() : CommonImportBatchStatusEnum.IMPORT_FAILED.getCode());
        importBatch.setErrorFilePath(errorFilePath);
        importBatch.setCustomerCode(sysUser.getCustomerCode());
        importBatch.setCreateBy(sysUser.getUserName());
        importBatch.setUpdateBy(sysUser.getUserName());
        return importBatch;
    }

    /**
     * 下载导入模板
     * @param response 响应
     * @param importType 导入类型
     * @throws IOException 异常
     */
    public void downloadTemplate(HttpServletResponse response, String importType) throws IOException {
        AbstractImportFileService<?> abstractImportFileService = this.getImportFileService(importType);
        String templateFilePath = abstractImportFileService.findTemplateFilePath();
        String fileName = FileUtil.getName(templateFilePath);
        aliyunOssUtil.downloadFile(response, templateFilePath, fileName);
    }
}
