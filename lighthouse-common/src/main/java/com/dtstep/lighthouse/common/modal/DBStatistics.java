package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;

public class DBStatistics implements Serializable {

    private Integer k;

    private Integer v;

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
