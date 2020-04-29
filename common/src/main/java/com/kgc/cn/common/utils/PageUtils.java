package com.kgc.cn.common.utils;

import lombok.Data;

import java.util.Collection;

@Data
public class PageUtils<T> {
    //当前页
    private int currentPage;
    //一页多少条
    private int pageSize=10;
    //返回对象
    private Collection<T> currentList;
    //一共多少条
    private long totalCount;
    //总页数
    private long totalPage = 0l;
    //页码
    private int pageNo;

    public int getPageNo() {
        return pageNo * pageSize - pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalPage() {
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }
        return totalPage;
    }


}
