package com.dtstep.lighthouse.core.rowkey;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.modal.Stat;

public interface KeyGenerator {

    String resultKey(Stat stat, int functionIndex, String dimens, long batchTime);

    String dimensKey(GroupExtEntity groupExtEntity, String dimens, String dimensValue);
}
