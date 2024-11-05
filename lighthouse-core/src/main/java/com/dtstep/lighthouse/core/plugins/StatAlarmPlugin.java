package com.dtstep.lighthouse.core.plugins;

import com.dtstep.lighthouse.common.entity.LdpNotification;
import com.dtstep.lighthouse.common.entity.event.AlarmEvent;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.modal.Alarm;

import java.util.List;

public interface StatAlarmPlugin extends Plugin {

    void trigger(StatExtEntity statExtEntity, long batchTime, String dimensValue);
}
