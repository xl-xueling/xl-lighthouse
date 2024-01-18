package com.dtstep.lighthouse.insights.dto_bak;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ListSearchObject<T> {

    private T queryParams;

    @NotNull
    private Pagination pagination;

    public T getQueryParams() {
        return queryParams;
    }

    public T getQueryParamOrDefault(T defaultValue) {
        if(queryParams == null){
            return defaultValue;
        }else{
            return queryParams;
        }
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
