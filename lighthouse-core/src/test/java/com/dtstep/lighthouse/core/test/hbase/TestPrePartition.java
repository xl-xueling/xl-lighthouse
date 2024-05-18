package com.dtstep.lighthouse.core.test.hbase;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.ListUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class TestPrePartition {

    @Test
    public void testPrePartition() throws Exception {
        int prePartitionsSize = 8;
        String [] keys = SysConst._DBKeyPrefixArray;
        List<String> keysList = Arrays.asList(keys);
        List<List<String>> totalGroupKeyList = ListUtil.listPartition(keysList,prePartitionsSize);
        for(int i=0;i<totalGroupKeyList.size();i++){
            List<String> groupList = totalGroupKeyList.get(i);
            for(int n=0;n<groupList.size();n++){
                System.out.println("group:" + i + ",index:" + n + ",key:" + groupList.get(n));
            }
        }
        byte[][] splitKeys = new byte[prePartitionsSize][];
        TreeSet<byte[]> rows = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < prePartitionsSize; i++) {
            String key = totalGroupKeyList.get(i).get(0);
            rows.add(Bytes.toBytes(key));
        }
        for(int i=0;i<totalGroupKeyList.size();i++){
            List<String> subList = totalGroupKeyList.get(i);
            for(int n=0;n<subList.size();n++){
                String temp = subList.get(n);
                System.out.println("group:" + i + ",index:" + n + ",value:" + temp);
            }
        }

        System.out.println("totalGroupKeyList size:" + totalGroupKeyList.size());
    }
}
