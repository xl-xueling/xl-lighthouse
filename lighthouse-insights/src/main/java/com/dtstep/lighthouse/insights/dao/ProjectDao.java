package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public interface ProjectDao {

    int insert(Project project);

    List<Project> queryList(ProjectQueryParam queryParam,Integer pageNum,Integer pageSize);
}
