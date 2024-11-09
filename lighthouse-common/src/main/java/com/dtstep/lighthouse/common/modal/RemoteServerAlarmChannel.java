package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;
import java.util.List;

public class RemoteServerAlarmChannel implements Serializable {

    private List<String> serverList;

    private List<KV<String,String>> headers;

    private String template;

    private int fuseSeconds;

    private boolean state;

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
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
}
