package com.dtstep.lighthouse.core.storage.common;

public class LdpGet {

    private String key;

    private String column;

    public static LdpGet with(String key,String column){
        LdpGet ldpGet = new LdpGet();
        ldpGet.setKey(key);
        ldpGet.setColumn(column);
        return ldpGet;
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
}
