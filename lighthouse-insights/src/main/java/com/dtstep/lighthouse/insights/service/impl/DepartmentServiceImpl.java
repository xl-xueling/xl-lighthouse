package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dto_bak.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Resource;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.*;
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

    @Autowired
    private DomainService domainService;

    @Transactional
    @Override
    public int create(Department department) {
        Date date = new Date();
        department.setUpdateTime(date);
        department.setCreateTime(date);
        departmentDao.insert(department);
        int departmentId = department.getId();
        List<Role> roleList = new ArrayList<>();
        Integer resourcePid;
        if(department.getPid() == 0){
            resourcePid = domainService.queryDefault().getId();
            resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Department,departmentId,ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Department,departmentId,ResourceTypeEnum.Department,resourcePid));
        }
        return department.getId();
    }

    @Transactional
    @Override
    public int update(Department department) {
        int result = departmentDao.update(department);
        Integer resourcePid;
        if(department.getPid() == 0){
            resourcePid = domainService.queryDefault().getId();
            resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Department,resourcePid));
        }
        return result;
    }

    @Transactional
    @Override
    public int delete(Department department) {
        Validate.notNull(department);
        int result = departmentDao.deleteById(department.getId());
        Integer resourcePid;
        if(department.getPid() == 0){
            resourcePid = domainService.queryDefault().getId();
            resourceService.deleteResourceCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.deleteResourceCallback(Resource.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Department,resourcePid));
        }
        return result;
    }

    @Override
    public Department queryById(Integer id) {
        return departmentDao.queryById(id);
    }

    @Override
    public List<TreeNode> getStructure() {
        List<Department> departmentList = queryAll();
        HashMap<Integer,TreeNode> departmentMap = new HashMap<>();
        for (Department department : departmentList) {
            TreeNode treeNode = new TreeNode(department.getName(),department.getId());
            departmentMap.put(department.getId(),treeNode);
        }
        List<TreeNode> nodeList = new ArrayList<>();
        for(Department department:departmentList){
            TreeNode currentNode = departmentMap.get(department.getId());
            int pid = department.getPid();
            if(pid == 0){
                nodeList.add(currentNode);
            }else{
                TreeNode parentNode = departmentMap.get(pid);
                if(parentNode != null){
                    List<TreeNode> children = (parentNode.getChildren() == null ? new ArrayList<>() : parentNode.getChildren());
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

    @Override
    public String getFullPath(Integer id) {
        return departmentDao.getFullPath(id);
    }
}
