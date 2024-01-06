package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.ProjectCreateParam;
import com.dtstep.lighthouse.insights.dto.ProjectDto;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public interface ProjectService {

    int create(ProjectCreateParam project);

    int update(Project project);

    ProjectDto queryById(Integer id);

    List<CommonTreeNode> getStructure(Project project);

    int delete(Project project);

    ListData<ProjectDto> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize);

    int countByDepartmentId(Integer departmentId);
}
