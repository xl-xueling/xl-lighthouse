package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.role.PermissionsEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.ProjectDto;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public int create(Project project){
        Date date = new Date();
        project.setUpdateTime(date);
        project.setCreateTime(date);
        projectDao.insert(project);
        return project.getId();
    }

    @Override
    public List<Project> queryById(Integer id) {
        return null;
    }

    @Override
    public ListData<ProjectDto> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Project> projectList = projectDao.queryList(queryParam,pageNum,pageSize);
        List<ProjectDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(projectList)){
            for(Project project : projectList){
                ProjectDto projectDto = new ProjectDto(project);
                int departmentId = project.getDepartmentId();
                Department department = departmentDao.queryById(departmentId);
                projectDto.setDepartment(department);
                projectDto.setPermissions(List.of(PermissionsEnum.ADMIN));
                dtoList.add(projectDto);
            }
        }
        ListData<ProjectDto> listData = new ListData<>();
        listData.setList(dtoList);
        return listData;
    }
}
