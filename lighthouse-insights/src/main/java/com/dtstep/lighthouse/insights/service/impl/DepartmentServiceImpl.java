package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public int create(Department department) {
        Date date = new Date();
        department.setUpdateTime(date);
        department.setCreateTime(date);
        departmentDao.insert(department);
        return department.getId();
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
