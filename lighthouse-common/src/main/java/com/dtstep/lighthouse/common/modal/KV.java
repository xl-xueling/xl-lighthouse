package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;

public final class KV<K,V> implements Serializable {

    private K k;

    private V v;

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
}
