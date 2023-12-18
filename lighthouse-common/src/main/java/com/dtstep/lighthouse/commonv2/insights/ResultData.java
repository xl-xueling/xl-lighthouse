package com.dtstep.lighthouse.commonv2.insights;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResultData {

    private String code;

    private String message;

    private ObjectNode data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectNode getData() {
        return data;
    }

    public void setData(ObjectNode data) {
        this.data = data;
    }
}
