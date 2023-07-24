package com.dtstep.lighthouse.common.lru;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class LRUCacheTest {

    @Test
    public void testPut() throws Exception {
        Cache<String,String> cache = LRU.newBuilder().maximumSize(100).expireAfterWrite(10,TimeUnit.SECONDS).build();
        cache.put("a","123");
        Thread.sleep(5000);
        cache.put("b","123");
        cache.put("c","123");
        Thread.sleep(5000);
        cache.put("d","123");
        Thread.sleep(5000);
        cache.put("e","123");
        Thread.sleep(5000);
        cache.put("f","123");
        for(int i=0;i<1000;i++){
            String av = cache.get("a");
            String bv = cache.get("b");
            String cv = cache.get("c");
            String dv = cache.get("d");
            String ev = cache.get("e");
            String fv = cache.get("f");
            System.out.println("i" + i+",av is:" + av + ",bv is:" + bv + ",cv:" + cv + ",dv:" + dv + ",ev:" + ev + ",fv:" + fv);
            Thread.sleep(1000);
        }
    }

    @Test
    public void remove() throws Exception {
        Cache<String,String> cache = LRU.newBuilder().maximumSize(100).expireAfterWrite(10,TimeUnit.SECONDS).build();
        cache.put("a","123");
        cache.put("b","123");
        cache.put("c","123");
        System.out.println(cache.toString());
        cache.remove("c");
        System.out.println(cache.toString());

    }
}
