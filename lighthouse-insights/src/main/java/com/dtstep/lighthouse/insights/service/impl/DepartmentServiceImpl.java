package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dto_bak.CommonTreeNode;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Resource;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectService projectService;

    @Transactional
    @Override
    public int create(Department department) {
        Date date = new Date();
        department.setUpdateTime(date);
        department.setCreateTime(date);
        departmentDao.insert(department);
        int departmentId = department.getId();
        List<Role> roleList = new ArrayList<>();
        resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Department,departmentId,department.getPid()));
        return department.getId();
    }

    @Transactional
    @Override
    public int update(Department department) {
        int result = departmentDao.update(department);
        resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),department.getPid()));
        return result;
    }

    @Transactional
    @Override
    public int delete(Department department) {
        Validate.notNull(department);
        int result = departmentDao.deleteById(department.getId());
        resourceService.deleteResourceCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),department.getPid()));
        return result;
    }

    @Override
    public Department queryById(Integer id) {
        return departmentDao.queryById(id);
    }

    @Override
    public List<CommonTreeNode> queryTreeFormat() {
        List<Department> departmentList = queryAll();
        HashMap<Integer,CommonTreeNode> departmentMap = new HashMap<>();
        for (Department department : departmentList) {
            CommonTreeNode commonTreeNode = new CommonTreeNode();
            commonTreeNode.setId(String.valueOf(department.getId()));
            commonTreeNode.setPid(String.valueOf(department.getPid()));
            commonTreeNode.setName(department.getName());
            departmentMap.put(department.getId(),commonTreeNode);
        }
        List<CommonTreeNode> nodeList = new ArrayList<>();
        for(Department department:departmentList){
            CommonTreeNode currentNode = departmentMap.get(department.getId());
            int pid = department.getPid();
            if(pid == 0){
                nodeList.add(currentNode);
            }else{
                CommonTreeNode parentNode = departmentMap.get(pid);
                if(parentNode != null){
                    List<CommonTreeNode> children = (parentNode.getChildren() == null ? new ArrayList<>() : parentNode.getChildren());
                    children.add(currentNode);
                    parentNode.setChildren(children);
                }else{
                    nodeList.add(currentNode);
                }
            }
        }
        return nodeList;
    }

    @Override
    public List<Department> queryAll() {
        return departmentDao.queryAll();
    }

    @Override
    public int getLevel(Integer id) {
        return departmentDao.getLevel(id);
    }

    @Override
    public int getChildLevel(Integer pid) {
        return departmentDao.getChildLevel(pid);
    }

    @Override
    public int countChildByPid(Integer pid) {
        return departmentDao.countChildByPid(pid);
    }
}
