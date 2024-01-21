package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.vo.ComponentVO;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.modal.Component;

public interface ComponentService {

    ResultCode verify(String configuration);

    int create(Component component);

    int delete(Component component);

    Component queryById(Integer id);

    int update(Component component);

    ListData<ComponentVO> queryList(ComponentQueryParam queryParam, Integer pageNum, Integer pageSize);
}
