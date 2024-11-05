package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;

import java.util.List;

public class LdpNotification {

    private Integer alarmId;

    private List<String> reasons;

    private NotificationTypeEnum notificationType;

    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
