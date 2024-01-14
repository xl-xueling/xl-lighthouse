package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import org.springframework.data.repository.query.Param;

public interface MetricSetService {

    Integer create(MetricSet metricSet);

    MetricSet queryById(Integer id);

    ListData<MetricSet> queryList(MetricSetQueryParam queryParam,Integer pageNum,Integer pageSize);
}
