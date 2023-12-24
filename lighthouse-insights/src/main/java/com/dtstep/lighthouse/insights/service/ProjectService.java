package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public interface ProjectService {

    int create(Project project);

    List<Project> queryById(Integer id);

    ListData<Project> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize);

}
