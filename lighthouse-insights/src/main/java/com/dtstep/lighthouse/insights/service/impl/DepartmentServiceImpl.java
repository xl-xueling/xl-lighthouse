package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.DepartmentService;
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

    @Transactional
    @Override
    public int create(Department department) {
        Date date = new Date();
        department.setUpdateTime(date);
        department.setCreateTime(date);
        departmentDao.insert(department);
        int departmentId = department.getId();
        List<Role> roleList = new ArrayList<>();
        int pid = department.getPid();
        Integer manageRolePid = 0;
        Integer accessRolePid = 0;
        Role parentManageRole = roleService.queryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,pid);
        manageRolePid = parentManageRole.getId();
        Role parentAccessRole = roleService.queryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,pid);
        accessRolePid = parentAccessRole.getId();
        Role manageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,departmentId,manageRolePid);
        Role accessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,departmentId,accessRolePid);
        roleList.add(manageRole);
        roleList.add(accessRole);
        roleService.batchCreate(roleList);
        return 0;
    }

    @Override
    public int update(Department department) {
        return departmentDao.update(department);
    }

    @Override
    public int deleteById(List<Integer> ids) {
        return departmentDao.deleteById(ids);
    }

    @Override
    public Department queryById(Integer id) {
        return departmentDao.queryById(id);
    }

    @Override
    public List<CommonTreeNode> queryAll() {
        List<Department> departmentList = departmentDao.queryAll();
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
}
