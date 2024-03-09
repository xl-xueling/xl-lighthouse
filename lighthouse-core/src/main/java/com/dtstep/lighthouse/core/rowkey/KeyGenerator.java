package com.dtstep.lighthouse.core.rowkey;

import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Stat;

import java.io.Serializable;

public interface KeyGenerator extends Serializable {

    String resultKey(Stat stat, int functionIndex, String dimensValue, long batchTime);

    String dimensKey(Group group, String dimens, String dimensValue);
}
