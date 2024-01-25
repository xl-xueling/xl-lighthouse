package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.MetricBindParam;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;

import java.util.List;

public interface MetricSetService {

    int create(MetricSet metricSet);

    int binded(MetricBindParam bindParam);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    int update(MetricSet metricSet);

    MetricSetVO queryById(Integer id);

    ListData<MetricSet> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<TreeNode> getStructure(MetricSet metricSet) throws Exception;
}
