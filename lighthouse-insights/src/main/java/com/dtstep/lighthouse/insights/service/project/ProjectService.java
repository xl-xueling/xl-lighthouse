package com.dtstep.lighthouse.insights.service.project;

import com.dtstep.lighthouse.insights.dto.project.ProjectDto;
import com.dtstep.lighthouse.insights.dto.project.ProjectSearchParams;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> queryById(int id) throws Exception;

    List<ProjectDto> queryListByPage(int page, int limit, ProjectSearchParams searchParams) throws Exception;

}
