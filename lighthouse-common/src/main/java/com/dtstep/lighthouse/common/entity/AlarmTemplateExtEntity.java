package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.AlarmTemplate;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

import java.time.LocalTime;
import java.util.List;

public class AlarmTemplateExtEntity extends AlarmTemplate {

    private AlarmTemplateConfig templateConfig;

    public AlarmTemplateExtEntity(){}

    public AlarmTemplateExtEntity(AlarmTemplate alarmTemplate){
        assert alarmTemplate != null;
        BeanCopyUtil.copy(alarmTemplate,this);
    }

    public AlarmTemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(AlarmTemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    public static class AlarmTemplateConfig {

        private List<Integer> weekdays;

        private LocalTime startTime;

        private LocalTime endTime;

        public List<Integer> getWeekdays() {
            return weekdays;
        }

        public void setWeekdays(List<Integer> weekdays) {
            this.weekdays = weekdays;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }
    }
}
