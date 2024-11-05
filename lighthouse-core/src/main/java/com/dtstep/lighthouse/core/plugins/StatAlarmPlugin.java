package com.dtstep.lighthouse.core.plugins;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;

public interface StatAlarmPlugin extends Plugin {

    void trigger(StatExtEntity statExtEntity, long batchTime, String dimensValue);
}
