package com.dtstep.lighthouse.insights.dto_bak;

import java.io.Serializable;

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
