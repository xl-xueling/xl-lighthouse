package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.LanguageEnum;

import java.io.Serializable;
import java.util.List;

public class RemoteServerAlarmChannel implements Serializable {

    private List<String> servers;

    private List<KV<String,String>> headers;

    private String template;

    private int fuseSeconds;

    private boolean state;

    private LanguageEnum lang = LanguageEnum.CHINESE;

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public List<KV<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<KV<String, String>> headers) {
        this.headers = headers;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getFuseSeconds() {
        return fuseSeconds;
    }

    public void setFuseSeconds(int fuseSeconds) {
        this.fuseSeconds = fuseSeconds;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public LanguageEnum getLang() {
        return lang;
    }

    public void setLang(LanguageEnum lang) {
        this.lang = lang;
    }
}
