package com.dtstep.lighthouse.core.rowkey;

public interface KeyGenerator {

    String resultKey(String statMapperId, int dataVersion, int functionIndex, String dimens, long batchTime);

    String dimensKey(String groupMapperId,String dimens,String dimensValue);
}
