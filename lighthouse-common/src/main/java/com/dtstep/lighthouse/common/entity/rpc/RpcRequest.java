package com.dtstep.lighthouse.common.entity.rpc;

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private String requestId;

    private String className;

    private String methodName;

    private RpcMsgType type;

    private Class<?>[] parameterTypes;

    private Object[] parameterValues;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RpcMsgType getType() {
        return type;
    }

    public void setType(RpcMsgType type) {
        this.type = type;
    }
}
