package com.dtstep.lighthouse.core.preparing.pipe;

public class PreparingInput<T>
{
    public T data;

    public PreparingInput(T obj)
    {
        this.data = obj;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
