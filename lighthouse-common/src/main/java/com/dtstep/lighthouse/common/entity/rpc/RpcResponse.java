package com.dtstep.lighthouse.common.entity.rpc;

import java.io.Serializable;

public class RpcResponse<T> implements Serializable {

    private String requestId;

    private String error;

    private RpcMsgType type;

    private T result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public RpcMsgType getType() {
        return type;
    }

    public void setType(RpcMsgType type) {
        this.type = type;
    }
}
