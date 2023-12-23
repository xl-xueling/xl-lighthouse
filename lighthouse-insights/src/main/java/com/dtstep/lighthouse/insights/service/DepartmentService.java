package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.modal.Department;

import java.util.List;

public interface DepartmentService {

    int create(Department department);

    int update(Department department);

    int deleteById(List<Integer> ids);

    Department queryById(Integer id);

    List<CommonTreeNode> queryAll();
}
