package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.hash.CityHash;
import org.junit.Test;

public class CityHashTest {

    @Test
    public void cityHash() throws Exception {
        long[] s = CityHash.cityHash128("test".getBytes(),0,4);
        for(int i=0;i<s.length;i++){
            System.out.println("length:" + s[i]);
        }

        long t = CityHash.cityHash64("test".getBytes(),0,4);
        System.out.println("t:" + t);
    }
}
