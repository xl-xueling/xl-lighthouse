package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public interface ProjectService {

    int create(Project project);

    List<Project> queryById(int id);

    List<Project> queryListByPage(int page, int limit, ProjectQueryParam searchParams);

}
