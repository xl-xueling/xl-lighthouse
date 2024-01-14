package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricSetDao {

    Integer insert(MetricSet metricSet);

    MetricSet queryById(Integer id);

    ListData<MetricSet> queryList(@Param("queryParam")MetricSetQueryParam queryParam);
}
