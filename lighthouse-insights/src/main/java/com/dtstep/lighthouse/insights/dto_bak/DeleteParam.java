package com.dtstep.lighthouse.insights.dto_bak;

import java.io.Serializable;
import java.util.List;

public class DeleteParam implements Serializable {

    private Integer id;

    private List<Integer> ids;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
