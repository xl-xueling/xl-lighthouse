package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricSetDao {

    int insert(MetricSet metricSet);

    int update(MetricSet metricSet);

    MetricSet queryById(Integer id);

    List<MetricSet> queryList(@Param("queryParam")MetricSetQueryParam queryParam);
}
