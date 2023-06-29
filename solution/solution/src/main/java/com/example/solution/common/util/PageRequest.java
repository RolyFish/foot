package com.example.solution.common.util;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页请求对象
 *
 * @author wuyuhao
 * @date 2022/8/20 10:14
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码信息，默认1
     */

    @Min(value = 1, message = "The minimum value of pageNum is 1!")
    private Integer pageNum;

    /**
     * 每页记录数，默认50，最大1000
     */
    @Max(value = 1000, message = "The maximum value of pageSize is 1000!")
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String orderByColumn;

    /**
     * 排序的方向 "desc" 或者 "asc"
     */
    private String orderByType;

    /**
     * 分页参数合理化
     */
    private Boolean reasonable;
}
