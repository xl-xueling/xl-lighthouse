package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.common.modal.Indicator;
import com.dtstep.lighthouse.common.modal.MetricSet;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;

import java.util.List;

public interface MetricSetService {

    int create(MetricSet metricSet);

    int delete(MetricSet metricSet);

    ResultCode star(MetricSet metricSet);

    ResultCode unStar(MetricSet metricSet);

    int binded(MetricBindParam bindParam);

    int bindRemove(MetricBindRemoveParam removeParam);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    int update(MetricSet metricSet);

    MetricSetVO queryById(Integer id);

    ListData<MetricSetVO> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(MetricSetQueryParam queryParam);

    List<MetricSetVO> queryStarList();

    TreeNode getStructure(MetricSet metricSet) throws Exception;

    void updateStructure(MetricUpdateStructureParam updateStructureParam);

    ListData<Indicator> queryIndicatorList(MetricPendQueryParam queryParam, Integer pageNum, Integer pageSize);
}
