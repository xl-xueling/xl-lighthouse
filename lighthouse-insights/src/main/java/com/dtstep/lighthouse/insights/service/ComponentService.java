package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.vo.ComponentVO;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.common.modal.Component;

public interface ComponentService {

    ResultCode verify(String configuration);

    int create(Component component);

    int delete(Component component);

    Component queryById(Integer id);

    int update(Component component);

    ListData<ComponentVO> queryList(ComponentQueryParam queryParam, Integer pageNum, Integer pageSize);
}
