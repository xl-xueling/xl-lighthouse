package com.dtstep.lighthouse.insights.service.project.impl;

import com.dtstep.lighthouse.insights.dto.project.ProjectDto;
import com.dtstep.lighthouse.insights.dto.project.ProjectSearchParams;
import com.dtstep.lighthouse.insights.service.project.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<ProjectDto> queryById(int id) throws Exception {
        return null;
    }

    @Override
    public List<ProjectDto> queryListByPage(int page, int limit, ProjectSearchParams searchParams) throws Exception {
        return null;
    }
}
