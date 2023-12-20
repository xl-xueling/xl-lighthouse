package com.dtstep.lighthouse.insights.modal;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

    private Integer id;

    @NotEmpty
    private String token;

    private List<Column> columns;

    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
