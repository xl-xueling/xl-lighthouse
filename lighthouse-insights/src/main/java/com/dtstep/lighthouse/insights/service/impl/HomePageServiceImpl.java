package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.DBStatistics;
import com.dtstep.lighthouse.common.modal.Department;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.HomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private MetricSetService metricSetService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryOverview'",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public HomeVO queryOverview() {
        ProjectQueryParam totalProjectQueryParam = new ProjectQueryParam();
        int projectCount = projectService.count(totalProjectQueryParam);
        long timestamp = DateUtil.getDayBefore(System.currentTimeMillis(),1);
        long ytdStartTime = DateUtil.getDayStartTime(timestamp);
        long ytdEndTime = DateUtil.getDayEndTime(timestamp);
        ProjectQueryParam ytdProjectQueryParam = new ProjectQueryParam();
        ytdProjectQueryParam.setCreateStartTime(DateUtil.timestampToLocalDateTime(ytdStartTime));
        ytdProjectQueryParam.setCreateEndTime(DateUtil.timestampToLocalDateTime(ytdEndTime));
        int ytdProjectCount = projectService.count(ytdProjectQueryParam);
        StatQueryParam totalStatQueryParam = new StatQueryParam();
        int statCount = statService.count(totalStatQueryParam);
        StatQueryParam ytdStatQueryParam = new StatQueryParam();
        ytdStatQueryParam.setCreateStartTime(DateUtil.timestampToLocalDateTime(ytdStartTime));
        ytdStatQueryParam.setCreateEndTime(DateUtil.timestampToLocalDateTime(ytdEndTime));
        int ytdStatCount = statService.count(ytdStatQueryParam);
        MetricSetQueryParam totalMetricQueryParam = new MetricSetQueryParam();
        int metricCount = metricSetService.count(totalMetricQueryParam);
        MetricSetQueryParam ytdMetricQueryParam = new MetricSetQueryParam();
        ytdMetricQueryParam.setCreateStartTime(DateUtil.timestampToLocalDateTime(ytdStartTime));
        ytdMetricQueryParam.setCreateEndTime(DateUtil.timestampToLocalDateTime(ytdEndTime));
        int ytdMetricCount = metricSetService.count(ytdMetricQueryParam);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setStates(List.of(UserStateEnum.USER_NORMAL.getState()));
        int userCount = userService.count(userQueryParam);
        List<DBStatistics> statistics = statService.getTopDepartmentStatSize();
        List<StatValue> valueList = new ArrayList<>();
        List<Department> allDepartments = departmentService.queryAll();
        Map<Integer,Department> departmentMap = allDepartments.stream().collect(Collectors.toMap(Department::getId, x -> x));
        for(DBStatistics dbStatistics : statistics){
            int departmentId = dbStatistics.getK();
            int count = dbStatistics.getV();
            StatValue statValue = new StatValue();
            Department department = departmentMap.getOrDefault(departmentId,null);
            if(department != null){
                statValue.setDimensValue(department.getName());
                statValue.setValue(count);
                valueList.add(statValue);
            }
        }
        HomeVO homeVO = new HomeVO();
        homeVO.setProjectCount(projectCount);
        homeVO.setYtdProjectCount(ytdProjectCount);
        homeVO.setStatCount(statCount);
        homeVO.setYtdStatCount(ytdStatCount);
        homeVO.setMetricCount(metricCount);
        homeVO.setYtdMetricCount(ytdMetricCount);
        homeVO.setUserCount(userCount);
        homeVO.setDepartmentStatCount(valueList);
        return homeVO;
    }
}
