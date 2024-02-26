package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.modal.RenderConfig;
import com.dtstep.lighthouse.common.modal.RenderFilterConfig;
import com.dtstep.lighthouse.common.modal.Stat;

import java.util.List;

public interface StatService {

    ResultCode create(Stat stat);

    ResultCode update(Stat stat);

    int delete(Stat stat);

    StatVO queryById(Integer id);

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<StatVO> queryByIds(List<Integer> ids);

    int count(StatQueryParam queryParam);

    RenderConfig getStatRenderConfig(Stat stat);

    ResultCode filterConfig(Stat stat, List<RenderFilterConfig> filterConfigs);
}
