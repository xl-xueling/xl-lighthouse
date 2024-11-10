package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.LanguageEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AlarmTestingParam implements Serializable {

    @NotNull
    private String server;

    private LanguageEnum lang = LanguageEnum.CHINESE;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public LanguageEnum getLang() {
        return lang;
    }

    public void setLang(LanguageEnum lang) {
        this.lang = lang;
    }
}
