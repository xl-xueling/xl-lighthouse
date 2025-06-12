package com.dtstep.lighthouse.insights.controller;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
