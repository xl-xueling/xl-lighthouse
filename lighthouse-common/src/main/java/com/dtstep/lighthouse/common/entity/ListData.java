package com.dtstep.lighthouse.common.entity;

import java.util.List;

public class ListData<T> {

    private List<T> list;

    private long total;

    private int pageNum;

    private int pageSize;

    public ListData(){}


    public static <T>ListData<T> newInstance(List<T> list,long total,int pageNum,int pageSize){
        ListData<T> listData = new ListData<>();
        listData.setList(list);
        listData.setTotal(total);
        listData.setPageNum(pageNum);
        listData.setPageSize(pageSize);
        return listData;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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

}
