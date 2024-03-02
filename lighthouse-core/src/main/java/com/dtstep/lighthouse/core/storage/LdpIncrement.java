package com.dtstep.lighthouse.core.storage;

public class LdpIncrement {

    private String key;

    private String column;

    private long step;

    private long ttl;

    public static LdpIncrement with(String key,String column,long step,long ttl){
        LdpIncrement ldpIncrement = new LdpIncrement();
        ldpIncrement.setKey(key);
        ldpIncrement.setColumn(column);
        ldpIncrement.setStep(step);
        ldpIncrement.setTtl(ttl);
        return ldpIncrement;
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

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
