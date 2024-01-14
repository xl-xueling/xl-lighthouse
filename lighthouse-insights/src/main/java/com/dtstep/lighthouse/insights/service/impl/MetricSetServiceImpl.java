package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import org.springframework.stereotype.Service;

@Service
public class MetricSetServiceImpl implements MetricSetService {

    @Override
    public Integer create(MetricSet metricSet) {
        return null;
    }

    @Override
    public MetricSet queryById(Integer id) {
        return null;
    }

    @Override
    public ListData<MetricSet> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize) {
        return null;
    }
}
