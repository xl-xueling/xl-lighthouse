package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.GrantPermissionsParam;
import com.dtstep.lighthouse.insights.dto.MetricSetDto;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetricSetService {

    int create(MetricSet metricSet);

    int grantAccessPermissions(Integer id, List<Integer> initUsersPermission,List<Integer> initDepartmentsPermission);

    int update(MetricSet metricSet);

    MetricSetDto queryById(Integer id);

    ListData<MetricSet> queryList(MetricSetQueryParam queryParam,Integer pageNum,Integer pageSize);
}
