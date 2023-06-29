package com.example.solution.common.util;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author 
 * @date 2021/12/15
 */
public class RestfulPageInfo<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /** 页码信息，默认1 */
    private int pageNum;

    /** 每页记录数，默认50，最大1000*/
    private int pageSize;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<T> data;

    /** 错误信息*/
    private List<Error> error;

    /**
     * 表格数据对象
     */
    public RestfulPageInfo()
    {
    }

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public RestfulPageInfo(List<T> list, int total)
    {
        this.data = list;
        this.total = total;
    }

    /**
     * 分页
     *
     * @param code 消息状态码
     * @param msg 消息内容
     * @param error 错误信息
     */
    public RestfulPageInfo(int code, String msg, List<Error> error)
    {
        this.code = code;
        this.msg = msg;
        this.error = error;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> rows)
    {
        this.data = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Error> getError() {
        return error;
    }

    public void setError(List<Error> error) {
        this.error = error;
    }
}
