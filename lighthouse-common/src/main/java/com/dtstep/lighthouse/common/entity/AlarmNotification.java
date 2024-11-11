package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;

import java.util.List;

public class AlarmNotification {

    private String silentKey;

    private long silentSeconds;

    private AlarmExtEntity alarmEntity;

    private StatExtEntity statExtEntity;

    private Integer level;

    private Long batchTime;

    private String content;

    private List<String> reasons;

    private NotificationTypeEnum notificationType;

    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
    }

    public AlarmExtEntity getAlarmEntity() {
        return alarmEntity;
    }

    public void setAlarmEntity(AlarmExtEntity alarmEntity) {
        this.alarmEntity = alarmEntity;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public String getSilentKey() {
        return silentKey;
    }

    public void setSilentKey(String silentKey) {
        this.silentKey = silentKey;
    }

    public long getSilentSeconds() {
        return silentSeconds;
    }

    public void setSilentSeconds(long silentSeconds) {
        this.silentSeconds = silentSeconds;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Long batchTime) {
        this.batchTime = batchTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StatExtEntity getStatExtEntity() {
        return statExtEntity;
    }

    public void setStatExtEntity(StatExtEntity statExtEntity) {
        this.statExtEntity = statExtEntity;
    }
}
