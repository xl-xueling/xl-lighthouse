package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.RenderConfig;
import com.dtstep.lighthouse.insights.modal.RenderFilterConfig;
import com.dtstep.lighthouse.insights.modal.Stat;

import java.util.List;

public interface StatService {

    ResultCode create(Stat stat);

    int update(Stat stat);

    int delete(Stat stat);

    StatVO queryById(Integer id);

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(StatQueryParam queryParam);

    RenderConfig getStatRenderConfig(Stat stat);

    ResultCode filterConfig(Stat stat, List<RenderFilterConfig> filterConfigs);
}
