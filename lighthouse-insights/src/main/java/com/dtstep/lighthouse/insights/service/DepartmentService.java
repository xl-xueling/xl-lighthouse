package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.modal.Department;

import java.util.List;

public interface DepartmentService {

    int create(Department department);

    int update(Department department);

    int delete(Department department);

    int getLevel(Integer id);

    int getChildLevel(Integer pid);

    int countChildByPid(Integer pid);

    Department queryById(Integer id);

    List<TreeNode> getStructure();

    List<Department> queryAll();

    String getFullPath(Integer id);
}
