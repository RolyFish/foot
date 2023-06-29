package com.example.solution.common.file.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 通用文件导入-平台skuMapping导入
 *
 * 
 * @date 2022/3/28 10:35
 */
@Slf4j
public class PlatformSkuMappingImportFileServiceImpl extends AbstractImportFileService<PlatformSkuMappingImportDTO> {

    @Resource
    private IProductService productService;
    @Resource
    private ISkuMappingService skuMappingService;
    @Resource
    private ICustomerService customerService;
    @Resource
    private ISkuMappingWorkFlowHistoryService skuMappingWorkFlowHistoryService;

    public PlatformSkuMappingImportFileServiceImpl(String importType) {
        super(importType);
    }


    @Override
    protected CommonImportContext<PlatformSkuMappingImportDTO> prepareImportContext() {
        // 数据源上下文初始化
        Long customerId = SecurityUtils.getCustomerId();
        Customer customer = customerService.lambdaQuery().eq(Customer::getCustomerId, customerId).one();
        if (customer == null) {
            log.error(">>>>>>>>>>PlatformSkuMappingImportFileServiceImpl【prepareImportContext】>>>>>>>>>>没有关联对应的客户信息");
            throw new ServiceException("No corresponding customer information is associated");
        }
        CommonImportContext<PlatformSkuMappingImportDTO> context = new CommonImportContext<>();
        context.setExcelUtil(new ExcelUtil<>(PlatformSkuMappingImportDTO.class));
        context.setCustomer(customer);
        return context;
    }

    @Override
    public String validateFile(MultipartFile file) throws Exception {
        List<PlatformSkuMappingImportDTO> sourceList = getCurrentSource();
        Customer customer = getCurrentContext().getCustomer();
        //重复的平台sku
        List<String> repeatPlatformSkuList = sourceList.stream()
                .collect(Collectors.groupingBy(platformSkuMappingImportDTO -> (platformSkuMappingImportDTO.getPlatformSku() + platformSkuMappingImportDTO.getSalesPlatform()).toLowerCase()
                , Collectors.counting()))
                .entrySet().stream()
                .filter(entry->entry.getValue()>1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // 定义生成错误文件标记
        boolean errorFlag = false;
        for (PlatformSkuMappingImportDTO importDto : sourceList) {
            // 通用注解校验
            StringJoiner errorMsg = ImportFileUtil.invokeValidation(importDto);
            //平台sku重复校验
            validateRepeat(errorMsg, importDto, repeatPlatformSkuList);
            // 平台sku映射校验
            platformSkuMappingValidation(errorMsg, importDto, customer);
            importDto.setErrorMessage(errorMsg.toString());
            // 有错误提示flag置为true
            if (StrUtil.isNotEmpty(errorMsg.toString())) {
                errorFlag = true;
            }
        }
        if (errorFlag) {
            String errorFilePath = filePathConfig.getErrorFile();
            // 根据模板重新生成错误文件
            return getCurrentUtil().importExcelForRegenerate(errorFilePath, file, 3, sourceList);
        }
        return null;
    }

    /**
     * 校验平台sku是否重复
     *
     * @param errorMsg 错误信息
     * @param importDto 导入dto
     * @param repeatPlatformSkuList 平台sku集合
     */
    private void validateRepeat(StringJoiner errorMsg, PlatformSkuMappingImportDTO importDto, List<String> repeatPlatformSkuList) {
        // 平台sku+销售平台判断导入的数据是否重复
        if (CollUtil.contains(repeatPlatformSkuList, platFormSku -> StrUtil.equalsIgnoreCase(importDto.getPlatformSku() + importDto.getSalesPlatform(), platFormSku))) {
            errorMsg.add(MessageUtils.message("sku.mapping.platform.sku.repeat"));
        }
    }

    @Override
    public boolean saveValidData(CommonImportBatch importBatch) {
        List<PlatformSkuMappingImportDTO> sourceList = getCurrentSource();
        if (CollUtil.isEmpty(sourceList) || importBatch == null) {
            return false;
        }
        // 存在错误文件不进行导入
        if (StrUtil.isNotEmpty(importBatch.getErrorFilePath())) {
            return true;
        }
        List<SkuMapping> skuMappingList = buildSkuMapping(sourceList, importBatch);
        return skuMappingService.saveBatch(skuMappingList, 200) && saveSkuMappingOperation(skuMappingList);
    }

    /**
     * 记录操作日志
     *
     * @param skuMappingList 已经保存的skuMapping的集合
     * @return 是否保存进日志表中，成功返回true
     */
    private boolean saveSkuMappingOperation(List<SkuMapping> skuMappingList) {
        List<SkuMappingWorkFlowHistory> list = skuMappingList.stream().map(skuMapping -> {
            SkuMappingWorkFlowHistory history = new SkuMappingWorkFlowHistory();
            BeanUtils.copyProperties(skuMapping, history);
            history.setSkuMappingId(skuMapping.getId());
            history.setOperationType(OperationEnum.CREATE.getCode());
            history.setCreateBy(skuMapping.getCreatedBy());
            history.setUpdateBy(skuMapping.getUpdatedBy());
            return history;
        }).collect(Collectors.toList());
        return this.skuMappingWorkFlowHistoryService.saveBatch(list, 200);
    }

    /**
     * dto对象构建为entity
     *
     * @param list        dto集合
     * @param importBatch 导入批次对象
     * @return entity集合
     */
    private List<SkuMapping> buildSkuMapping(List<PlatformSkuMappingImportDTO> list, CommonImportBatch importBatch) {
        return list.stream().map(dto -> {
            SkuMapping skuMapping = new SkuMapping();
            BeanUtil.copyProperties(dto, skuMapping);
            skuMapping.setSalesPlatform(SalesPlatformEnum.getCodeByEnDescription(dto.getSalesPlatform()).orElse(null));
            skuMapping.setCustomerCode(importBatch.getCustomerCode());
            skuMapping.setImportBatchNo(importBatch.getImportBatchNo());
            skuMapping.setImportBatchId(importBatch.getId());
            skuMapping.setCreatedBy(importBatch.getCreateBy());
            skuMapping.setUpdatedBy(importBatch.getCreateBy());
            skuMapping.setCreatedTime(new Date());
            skuMapping.setUpdatedTime(new Date());
            return skuMapping;
        }).collect(Collectors.toList());
    }

    /**
     * fedex运输单导入特殊校验- 默认已经做过非空校验
     *
     * @param joiner    拼接对象
     * @param importDto 导入对象
     * @param customer  客户对象
     */
    private void platformSkuMappingValidation(StringJoiner joiner,
                                              PlatformSkuMappingImportDTO importDto,
                                              Customer customer) {
        String customerCode = customer.getCustomerCode();
        if (StrUtil.isNotBlank(importDto.getCustomerSku())) {
            // 1.校验客户SKU是否存在
            Product product = productService.checkExistProduct(customer.getCustomerId(), importDto.getCustomerSku());
            if (product == null) {
                joiner.add(MessageUtils.message("sku.mapping.customer.sku.absent"));
                // 错误提示
            } else {
                importDto.setSystemSku(product.getProductCode());
            }
        }
        // 2.校验销售平台+平台SKU
        SkuMapping skuMapping = skuMappingService.findByPlatformInfo(importDto.getSalesPlatform(), importDto.getPlatformSku(), customerCode);
        if (skuMapping != null) {
            //错误提示
            joiner.add(MessageUtils.message("sku.mapping.platform.sku.exist"));
        }

    }
}
