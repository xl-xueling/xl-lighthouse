package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.modal.KeyValue;

import java.io.Serializable;
import java.util.List;

public class RequestBodyDTO implements Serializable {

    private String type;

    private List<KeyValue> content;

    private Object json;

    private String xml;

    private String raw;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<KeyValue> getContent() {
        return content;
    }

    public void setContent(List<KeyValue> content) {
        this.content = content;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
