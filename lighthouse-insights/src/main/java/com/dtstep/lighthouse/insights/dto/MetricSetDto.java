package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.User;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class MetricSetDto extends MetricSet {

    private List<User> admins;

    public MetricSetDto(MetricSet metricSet){
        Validate.notNull(metricSet);
        BeanCopyUtil.copy(metricSet,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }
}
