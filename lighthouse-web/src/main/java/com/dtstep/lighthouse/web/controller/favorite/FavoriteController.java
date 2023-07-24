package com.dtstep.lighthouse.web.controller.favorite;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.favorites.FavoriteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@ControllerAdvice
public class FavoriteController extends BaseController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping("/favorite/stat/list.shtml")
    public ModelAndView statList(HttpServletRequest request, ModelMap model) throws Exception{
        int userId = ParamWrapper.getCurrentUserId(request);
        List<ZTreeViewNode> nodeList = favoriteService.queryStatZTreeInfo(userId);
        if(CollectionUtils.isEmpty(nodeList)){
            model.addAttribute("link","/stat/list.shtml");
            model.addAttribute("isEmpty",true);
        }else{
            model.addAttribute("zNodeData", JsonUtil.valueToTree(nodeList));
        }
        return new ModelAndView("/favorite/favorite_stat",model);
    }

    @RequestMapping("/favorite/project/list.shtml")
    public ModelAndView projectList(HttpServletRequest request,ModelMap model) throws Exception{
        int userId = ParamWrapper.getCurrentUserId(request);
        List<ZTreeViewNode> nodeList = favoriteService.queryProjectZTreeInfo(userId);
        if(CollectionUtils.isEmpty(nodeList)){
            model.addAttribute("link","/project/list.shtml");
            model.addAttribute("isEmpty",true);
        }else{
            model.addAttribute("zNodeData",JsonUtil.valueToTree(nodeList));
        }
        return new ModelAndView("/favorite/favorite_project",model);
    }
}
