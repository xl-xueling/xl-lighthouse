package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.service.UserService;
import com.dtstep.lighthouse.insights.vo.HomeVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class HomePageController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private MetricSetService metricSetService;

    @Autowired
    private UserService userService;

    @RequestMapping("/homepage/overview")
    public ResultData<HomeVO> all() {
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
        HomeVO homeVO = new HomeVO();
        homeVO.setProjectCount(projectCount);
        homeVO.setYtdMetricCount(ytdProjectCount);
        homeVO.setStatCount(statCount);
        homeVO.setYtdStatCount(ytdStatCount);
        homeVO.setMetricCount(metricCount);
        homeVO.setYtdMetricCount(ytdMetricCount);
        homeVO.setUserCount(userCount);
        return ResultData.success(homeVO);
    }
}
