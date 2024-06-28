package com.dtstep.lighthouse.common.modal;

import javax.validation.constraints.NotNull;
import java.util.List;

public class IDParams {

    @NotNull
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
