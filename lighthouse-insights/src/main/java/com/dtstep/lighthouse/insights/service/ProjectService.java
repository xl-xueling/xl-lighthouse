package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto.PermissionReleaseParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.common.modal.Project;
import com.dtstep.lighthouse.insights.vo.ProjectVO;

import java.util.List;

public interface ProjectService {

    int create(Project project);

    int update(Project project);

    ProjectVO queryById(Integer id);

    ProjectVO cacheQueryById(Integer id);

    TreeNode getStructure(Project project) throws Exception;

    int delete(Project project);

    ListData<ProjectVO> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<ProjectVO> queryByIds(List<Integer> ids);

    int countByDepartmentId(Integer departmentId);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception;
}
