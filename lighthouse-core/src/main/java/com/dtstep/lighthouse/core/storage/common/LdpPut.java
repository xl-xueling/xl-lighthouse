package com.dtstep.lighthouse.core.storage.common;


public class LdpPut {

    private String key;

    private String column;

    private Object data;

    private long ttl;

    public static LdpPut with(String key,String column,Object data,long ttl){
        LdpPut ldpPut = new LdpPut();
        ldpPut.setKey(key);
        ldpPut.setColumn(column);
        ldpPut.setData(data);
        ldpPut.setTtl(ttl);
        return ldpPut;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data instanceof String || data instanceof Long) {
            this.data = data;
        } else {
            throw new IllegalArgumentException("LdpPut data attribute must be of type String or Long!");
        }
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
