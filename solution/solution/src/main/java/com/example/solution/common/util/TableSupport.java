package com.example.solution.common.util;

import com.gigacloud.starcloud.common.core.domain.PageRequest;
import com.gigacloud.starcloud.common.core.text.Convert;

/**
 * 表格数据处理
 *
 * @author wuyuhao
 * @date 2021/12/15
 */
public class TableSupport
{
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }


    /**
     * 封装分页对象
     * @param pageRequest 请求参数对象
     * @return 分页对象
     */
    public static PageDomain getPageDomain(PageRequest pageRequest)
    {
        PageDomain pageDomain = new PageDomain();
        pageRequest.setPageNum(pageRequest.getPageNum() == null ? 1 : pageRequest.getPageNum());
        pageRequest.setPageSize(pageRequest.getPageSize() == null ? 50 : pageRequest.getPageSize());
        pageDomain.setPageNum(pageRequest.getPageNum());
        pageDomain.setPageSize(pageRequest.getPageSize());
        pageDomain.setOrderByColumn(pageRequest.getOrderByColumn());
        pageDomain.setIsAsc(pageRequest.getOrderByType());
        pageDomain.setReasonable(pageRequest.getReasonable());
        return pageDomain;
    }

    public static PageDomain buildPageRequest(PageRequest pageRequest)
    {
        return getPageDomain(pageRequest);
    }
}
