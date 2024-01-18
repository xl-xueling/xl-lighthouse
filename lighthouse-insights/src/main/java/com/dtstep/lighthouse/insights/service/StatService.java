package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto_bak.StatDto;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.RenderConfig;
import com.dtstep.lighthouse.insights.modal.RenderFilterConfig;
import com.dtstep.lighthouse.insights.modal.Stat;

import java.util.List;

public interface StatService {

    int create(Stat stat);

    int update(Stat stat);

    int delete(Stat stat);

    StatDto queryById(Integer id);

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatDto> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);

    RenderConfig getStatRenderConfig(Stat stat);

    ResultCode filterConfig(Stat stat, List<RenderFilterConfig> filterConfigs);
}
