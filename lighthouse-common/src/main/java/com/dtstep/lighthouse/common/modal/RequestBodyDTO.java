package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;
import java.util.List;

public class RequestBodyDTO implements Serializable {

    private String type;

    private List<KeyValue> formData;

    private List<KeyValue> urlencodedData;

    private Object json;

    private String xml;

    private String raw;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<KeyValue> getFormData() {
        return formData;
    }

    public void setFormData(List<KeyValue> formData) {
        this.formData = formData;
    }

    public List<KeyValue> getUrlencodedData() {
        return urlencodedData;
    }

    public void setUrlencodedData(List<KeyValue> urlencodedData) {
        this.urlencodedData = urlencodedData;
    }
}
