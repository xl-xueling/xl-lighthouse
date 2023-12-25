package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.role.PermissionsEnum;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.GroupDto;
import com.dtstep.lighthouse.insights.dto.ProjectDto;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private GroupDao groupDao;

    @Override
    public int create(Project project){
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setUpdateTime(localDateTime);
        project.setCreateTime(localDateTime);
        projectDao.insert(project);
        return project.getId();
    }

    @Override
    public int update(Project project) {
        return projectDao.update(project);
    }

    @Override
    public ProjectDto queryById(Integer id) {
        Project project = projectDao.queryById(id);
        return translate(project);
    }

    private ProjectDto translate(Project project){
        ProjectDto projectDto = new ProjectDto(project);
        int departmentId = project.getDepartmentId();
        Department department = departmentDao.queryById(departmentId);
        projectDto.setDepartment(department);
        projectDto.setPermissions(List.of(PermissionsEnum.ADMIN));
        List<Group> dtoList = groupDao.queryByProjectId(project.getId());
        CommonTreeNode treeNode = new CommonTreeNode(String.valueOf(project.getId()),project.getTitle(), "0","1");
        if(CollectionUtils.isNotEmpty(dtoList)){
            List<CommonTreeNode> children = new ArrayList<>();
            treeNode.setChildren(children);
            for(Group group : dtoList){
                CommonTreeNode groupNode = new CommonTreeNode(String.valueOf(group.getId()),group.getToken(),String.valueOf(project.getId()),"2");
                children.add(groupNode);
            }
        }
        projectDto.setStructure(List.of(treeNode));
        return projectDto;
    }

    @Override
    public ListData<ProjectDto> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer userId = baseService.getCurrentUserId();
        System.out.println("userId:" + userId);
        List<Project> projectList = projectDao.queryList(queryParam,pageNum,pageSize);
        List<ProjectDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(projectList)){
            for(Project project : projectList){
                dtoList.add(translate(project));
            }
        }
        ListData<ProjectDto> listData = new ListData<>();
        listData.setList(dtoList);
        return listData;
    }
}
