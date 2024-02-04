package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.common.modal.Indicator;
import com.dtstep.lighthouse.common.modal.MetricSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricSetDao {

    int insert(MetricSet metricSet);

    int update(MetricSet metricSet);

    MetricSet queryById(Integer id);

    int deleteById(Integer id);

    List<MetricSet> queryList(@Param("queryParam")MetricSetQueryParam queryParam);

    int count(@Param("queryParam")MetricSetQueryParam queryParam);

    List<Indicator> queryIndicatorList(Integer id);
}
