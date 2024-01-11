package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ComponentDto;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.modal.Component;

public interface ComponentService {

    ResultCode verify(String configuration);

    int create(Component component);

    ListData<ComponentDto> queryList(ComponentQueryParam queryParam,Integer pageNum,Integer pageSize);
}
