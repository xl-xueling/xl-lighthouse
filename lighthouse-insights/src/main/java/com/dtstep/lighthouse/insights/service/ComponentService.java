package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;

public interface ComponentService {

    ResultCode verify(String configuration);
}
