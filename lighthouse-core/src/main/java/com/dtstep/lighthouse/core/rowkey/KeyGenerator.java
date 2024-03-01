package com.dtstep.lighthouse.core.rowkey;

import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Stat;

public interface KeyGenerator {

    String resultKey(Stat stat, int functionIndex, String dimensValue, long batchTime);

    String dimensKey(Group group, String dimens, String dimensValue);
}
