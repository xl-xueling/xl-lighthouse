package com.dtstep.lighthouse.standalone.rpc;

import java.io.Serializable;

public class RpcResponse<T> implements Serializable {

    private String requestId;

    private String error;

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
}
