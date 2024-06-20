package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ActivationParam implements Serializable {

    @NotNull
    private String organize;

    @NotNull
    private String installationCode;

    @NotNull
    private String activationCode;

    public String getOrganize() {
        return organize;
    }

    public void setOrganize(String organize) {
        this.organize = organize;
    }

    public String getInstallationCode() {
        return installationCode;
    }

    public void setInstallationCode(String installationCode) {
        this.installationCode = installationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
