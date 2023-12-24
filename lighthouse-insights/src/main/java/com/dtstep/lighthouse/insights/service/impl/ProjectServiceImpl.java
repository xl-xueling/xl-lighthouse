package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public int create(Project project){
        Date date = new Date();
        project.setUpdateTime(date);
        project.setCreateTime(date);
        projectDao.insert(project);
        return project.getId();
    }

    @Override
    public List<Project> queryById(int id){
        return null;
    }

    @Override
    public List<Project> queryListByPage(int page, int limit, ProjectQueryParam searchParams){
        return null;
    }
}
