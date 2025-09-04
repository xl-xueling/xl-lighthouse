package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;
import java.util.List;

public class HttpRequestConfig implements Serializable {

    private String method;

    private String url;

    private List<KeyValue> params;

    private List<KeyValue> headers;

    private RequestBodyDTO body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<KeyValue> getParams() {
        return params;
    }

    public void setParams(List<KeyValue> params) {
        this.params = params;
    }

    public List<KeyValue> getHeaders() {
        return headers;
    }

    public void setHeaders(List<KeyValue> headers) {
        this.headers = headers;
    }

    public RequestBodyDTO getBody() {
        return body;
    }

    public void setBody(RequestBodyDTO body) {
        this.body = body;
    }
}
