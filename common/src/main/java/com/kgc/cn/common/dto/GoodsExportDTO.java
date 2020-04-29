package com.kgc.cn.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoodsExportDTO extends BaseRowModel {
    @ExcelProperty(value = "商品ID", index = 0)
    private Long goodsid;

    @ExcelProperty(value = "商品描述", index = 1)
    private String goodsdescription;

    @ExcelProperty(value = "商品名", index = 2)
    private String goodsname;

    @ExcelProperty(value = "商品颜色", index = 3)
    private String goodscolor;

    @ExcelProperty(value = "商品库存", index = 4)
    private Integer goodscount;

}
