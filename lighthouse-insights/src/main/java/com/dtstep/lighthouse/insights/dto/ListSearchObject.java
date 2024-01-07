package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class ListSearchObject<T> implements Serializable {

    private T queryParams;

    private Pagination pagination;

    public T getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(T queryParams) {
        this.queryParams = queryParams;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
