//package com.example.solution.common.file.impl;
//
//
//import lombok.extern.slf4j.Slf4j;
//
//import javax.annotation.Resource;
//
///**
// * 通用文件导入-产品资料导入
// *
// *
// * @date 2022/3/28 10:35
// */
//@Slf4j
//public class ProductImportFileServiceImpl extends AbstractImportFileService<ProductImportDTO> {
//
//    @Resource
//    private IProductService productService;
//    @Resource
//    private ICustomerService customerService;
//
//    public ProductImportFileServiceImpl(String importType) {
//        super(importType);
//    }
//
//    @Override
//    protected CommonImportContext<ProductImportDTO> prepareImportContext() {
//        // 数据源上下文初始化
//        Long customerId = SecurityUtils.getCustomerId();
//        Customer customer = customerService.lambdaQuery().eq(Customer::getCustomerId, customerId).one();
//        if (customer == null) {
//            log.error(">>>>>>>>>>ProductImportFileServiceImpl【prepareImportContext】>>>>>>>>>>没有关联对应的客户信息");
//            throw new ServiceException("No corresponding customer information is associated");
//        }
//        CommonImportContext<ProductImportDTO> context = new CommonImportContext<>();
//        context.setExcelUtil(new ExcelUtil<>(ProductImportDTO.class));
//        context.setCustomer(customer);
//        return context;
//    }
//
//    @Override
//    public String validateFile(MultipartFile file) throws Exception {
//        List<ProductImportDTO> sourceList = getCurrentSource();
//        Customer customer = getCurrentContext().getCustomer();
//        // 定义生成错误文件标记
//        boolean errorFlag = false;
//        //查询用户输入的数据，是否有重复的客户sku
//        List<String> repeatSkuCode = sourceList.stream()
//                .collect(Collectors.groupingBy(productImportDTO -> productImportDTO.getCustomerSku().toLowerCase(), Collectors.counting()))
//                .entrySet().stream().filter(entry -> entry.getValue() > 1)
//                .map(Map.Entry::getKey).collect(Collectors.toList());
//
//        for (ProductImportDTO importDto : sourceList) {
//            // 通用注解校验
//            StringJoiner errorMsg = ImportFileUtil.invokeValidation(importDto);
//            //重复数据校验
//            repeatSkuCodeValidation(errorMsg, importDto, repeatSkuCode);
//            // 客户sku映射校验
//            productInfoValidation(errorMsg, importDto, customer);
//            //尺寸单位及其重量单位校验
//            productUnitValidation(customer, importDto, errorMsg);
//            importDto.setErrorMessage(errorMsg.toString());
//            //有错误提示flag置为true
//            if (StrUtil.isNotEmpty(errorMsg.toString())) {
//                errorFlag = true;
//            }
//        }
//
//        if (errorFlag) {
//            String errorFilePath = filePathConfig.getErrorFile();
//            // 根据模板重新生成错误文件
//            return getCurrentUtil().importExcelForRegenerate(errorFilePath, file, 3, sourceList);
//        }
//        return null;
//    }
//
//    /**
//     * 结合国别分别对导入表单信息中的尺寸、重量单位进行校验
//     * @param customer 客户信息
//     * @param importDto 导入表单信息
//     * @param errorMsg 错误信息
//     */
//    private void productUnitValidation(Customer customer, ProductImportDTO importDto, StringJoiner errorMsg) {
//        String sizeUnit = importDto.getSizeUnit(); //尺寸单位
//        String weightUnit = importDto.getWeightUnit();//重量单位
//        Integer country = customer.getCountry();
//        //国别为美国时，尺寸单位必须是inch，重量单位必须是lbs，非美国的，尺寸单位必须为cm，重量单位必须为kg
//        if(Objects.equals(country, CountryEnum.US.getCountry())) {
//            if(!ConvertUnitUtils.INCH.equalsIgnoreCase(sizeUnit)){
//                errorMsg.add(MessageUtils.message("product.size.unit.mismatch"));
//            }
//            if(!ConvertUnitUtils.LBS.equalsIgnoreCase(weightUnit)){
//                errorMsg.add(MessageUtils.message("product.weight.unit.mismatch"));
//            }
//        }else{
//            if(!ConvertUnitUtils.CM.equalsIgnoreCase(sizeUnit)){
//                errorMsg.add(MessageUtils.message("product.size.unit.mismatch"));
//            }
//            if(!ConvertUnitUtils.KG.equalsIgnoreCase(weightUnit)){
//                errorMsg.add(MessageUtils.message("product.weight.unit.mismatch"));
//            }
//        }
//    }
//
//    /**
//     * 客户sku是否有重复数据校验
//     *
//     * @param errorMsg      错误信息
//     * @param importDto     导入dto
//     * @param repeatSkuCode 重复sku集合
//     */
//    private void repeatSkuCodeValidation(StringJoiner errorMsg, ProductImportDTO importDto, List<String> repeatSkuCode) {
//        if (CollUtil.contains(repeatSkuCode, skuCode -> StrUtil.equalsIgnoreCase(skuCode, importDto.getCustomerSku()))) {
//            errorMsg.add(MessageUtils.message("product.customer.sku.repeat"));
//        }
//    }
//
//
//    @Override
//    public boolean saveValidData(CommonImportBatch importBatch) {
//        List<ProductImportDTO> sourceList = getCurrentSource();
//        if (CollUtil.isEmpty(sourceList) || importBatch == null) {
//            return false;
//        }
//        // 存在错误文件不进行导入
//        if (StrUtil.isNotEmpty(importBatch.getErrorFilePath())) {
//            return true;
//        }
//        saveProductAddVOList(sourceList, importBatch);
//        return true;
//    }
//
//    /**
//     * dto对象构建为entity
//     *
//     * @param list dto集合
//     */
//    private void saveProductAddVOList(List<ProductImportDTO> list, CommonImportBatch importBatch) {
//        Customer customer = customerService.findByCustomerCode(SecurityUtils.getCustomerCode());
//        list.forEach(productImportDTO -> {
//            ProductAddVO productAddVO = new ProductAddVO();
//            //顾客选中Y，code=1，选中N,code=0
//            productAddVO.setParts(YesNoEnum.getCodeByEnValue(productImportDTO.getPart()).orElse(null));
//            productAddVO.setIrregular(YesNoEnum.getCodeByEnValue(productImportDTO.getIrregular()).orElse(null));
//            productAddVO.setLtl(YesNoEnum.getCodeByEnValue(productImportDTO.getLtl()).orElse(null));
//            productAddVO.setSkuCode(productImportDTO.getCustomerSku());
//            productAddVO.setProductName(productImportDTO.getProductName());
//            productAddVO.setImportBatchNo(importBatch.getImportBatchNo());
//            productAddVO.setCustomerId(customer.getCustomerId().intValue());
//            productAddVO.setCountry(customer.getCountry());
//            productAddVO.setImportBatchId(importBatch.getId());
//            //顾客只能填写一组长，宽，高。且美国填写的是英镑，其他国家填写的是cm,后面统一转换单位
//            productAddVO.setLength(NumberUtil.toBigDecimal(productImportDTO.getLength()));
//            productAddVO.setWidth(NumberUtil.toBigDecimal(productImportDTO.getWidth()));
//            productAddVO.setHeight(NumberUtil.toBigDecimal(productImportDTO.getHeight()));
//            productAddVO.setWeight(NumberUtil.toBigDecimal(productImportDTO.getWeight()));
//            productService.addProduct(productAddVO);
//        });
//    }
//
//    /**
//     * 默认已经做过非空校验
//     *
//     * @param joiner    拼接对象
//     * @param importDto 导入对象
//     * @param customer  客户对象
//     */
//    private void productInfoValidation(StringJoiner joiner,
//                                       ProductImportDTO importDto,
//                                       Customer customer) {
//        Long customerId = customer.getCustomerId();
//        // 1.校验客户SKU是否存在
//        if (StrUtil.isNotBlank(importDto.getCustomerSku())) {
//            Product product = productService.getProductBySkuCodeAndCustomerId(importDto.getCustomerSku(), customerId);
//            if (product != null) {
//                joiner.add(MessageUtils.message("product.customer.sku.exist"));
//            }
//        }
//    }
//}
