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

    ResultCode create(Stat stat) throws Exception;

    ResultCode update(Stat stat);

    int delete(Stat stat);

    StatVO queryById(Integer id) throws Exception;

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<StatVO> queryByIds(List<Integer> ids) throws Exception;

    int count(StatQueryParam queryParam);

    RenderConfig getStatRenderConfig(StatVO stat) throws Exception;

    ResultCode filterConfig(StatVO stat, List<RenderFilterConfig> filterConfigs);
}
