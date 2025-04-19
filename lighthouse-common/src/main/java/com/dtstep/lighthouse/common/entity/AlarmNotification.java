package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;

import java.util.List;

public class AlarmNotification {

    private String silentKey;

    private long silentSeconds;

    private AlarmExtEntity alarmEntity;

    private StatExtEntity statExtEntity;

    private Integer priority;

    private Long time;

    private String dimensValue;

    private String operator;

    private double current;

    private double threshold;

    private String content;

    private String comparator;

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }
}
