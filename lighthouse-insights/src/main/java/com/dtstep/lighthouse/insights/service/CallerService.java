package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.CallerQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto.PermissionReleaseParam;
import com.dtstep.lighthouse.insights.vo.CallerVO;

import java.util.Map;

public interface CallerService {

    ServiceResult<Integer> create(Caller caller) throws Exception;

    int update(Caller caller) throws Exception;

    CallerVO queryById(Integer id) throws Exception;

    void deleteById(Integer id) throws Exception;

    String getSecretKey(Integer id);

    int count(CallerQueryParam queryParam) throws Exception;

    ListData<CallerVO> queryList(CallerQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception;

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception;
}
