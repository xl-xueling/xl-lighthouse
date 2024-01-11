package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.modal.Component;

public interface ComponentService {

    ResultCode verify(String configuration);

    ResultCode create(Component component);
}
