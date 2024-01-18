package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.ProjectCreateParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public interface ProjectService {

    int create(ProjectCreateParam project);

    List<User> cacheQueryAdmins(Integer id);

    int update(Project project);

    ProjectDto queryById(Integer id);

    TreeNode getStructure(Project project) throws Exception;

    int delete(Project project);

    ListData<ProjectDto> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize);

    int countByDepartmentId(Integer departmentId);
}
