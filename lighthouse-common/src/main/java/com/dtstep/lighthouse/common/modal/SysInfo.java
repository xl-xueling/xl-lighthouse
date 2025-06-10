package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;

public class SysInfo implements Serializable {

    private boolean proEdition = false;

    public boolean isProEdition() {
        return proEdition;
    }

    public void setProEdition(boolean proEdition) {
        this.proEdition = proEdition;
    }
}
