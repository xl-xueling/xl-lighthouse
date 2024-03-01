package com.dtstep.lighthouse.core.rowkey.impl;

import com.dtstep.lighthouse.core.rowkey.KeyGenerator;

public class DefaultKeyGenerator implements KeyGenerator {

    @Override
    public String resultKey(String statMapperId, int dataVersion, int functionIndex, String dimens, long batchTime) {
        return null;
    }


    @Override
    public String dimensKey(String groupMapperId, String dimens, String dimensValue) {
        return null;
    }
}
