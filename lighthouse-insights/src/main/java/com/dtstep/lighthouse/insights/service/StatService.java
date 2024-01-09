package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.ChangeStatStateParam;
import com.dtstep.lighthouse.insights.dto.StatDto;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.Stat;

import java.util.List;

public interface StatService {

    int create(Stat stat);

    int update(Stat stat);

    int delete(Stat stat);

    int changeState(ChangeStatStateParam changeStatStateParam);

    Stat queryById(Integer id);

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatDto> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);
}
