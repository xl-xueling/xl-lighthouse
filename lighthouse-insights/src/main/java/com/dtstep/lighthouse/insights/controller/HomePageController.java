package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.service.*;
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
    private HomePageService homePageService;

    @RequestMapping("/homepage/overview")
    public ResultData<HomeVO> all() {
        HomeVO homeVO = homePageService.queryOverview();
        return ResultData.success(homeVO);
    }
}
