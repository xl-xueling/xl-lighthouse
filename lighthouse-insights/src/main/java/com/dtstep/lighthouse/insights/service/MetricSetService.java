package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
import com.dtstep.lighthouse.insights.vo.ResourceVO;

import java.util.List;

public interface MetricSetService {

    int create(MetricSet metricSet);

    int delete(MetricSet metricSet);

    int binded(MetricBindParam bindParam);

    int bindRemove(MetricBindRemoveParam removeParam);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    int update(MetricSet metricSet);

    MetricSetVO queryById(Integer id);

    ListData<MetricSetVO> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<TreeNode> getStructure(MetricSet metricSet) throws Exception;

    void updateStructure(MetricUpdateStructureParam updateStructureParam);

    ListData<ResourceVO> queryPendList(MetricSet metricSet,Integer pageNum,Integer pageSize);
}
