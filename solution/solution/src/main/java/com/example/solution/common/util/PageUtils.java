package com.example.solution.common.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 *
 * 
 * @date 2022/01/12
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据,get传参模式
     */
    public static void startPage()
    {
        startPage(TableSupport.buildPageRequest());
    }


    /**
     * 设置请求分页数据，Post传参模式
     * @param pageRequest 请求参数对象
     */
    public static void startPage(PageRequest pageRequest)
    {
        startPage(TableSupport.buildPageRequest(pageRequest));
    }


    /**
     * 开始分页
     * @param pageDomain 分页对象
     */
    private static void startPage(PageDomain pageDomain) {
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            Boolean reasonable = pageDomain.getReasonable();
            PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        }
    }

    /**
     * 响应请求分页数据
     */
    public static TableDataInfo generateDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg(MessageUtils.message("user.query.success"));
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }



    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
