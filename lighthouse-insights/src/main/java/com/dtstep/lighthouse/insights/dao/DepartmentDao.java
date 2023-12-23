package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Department;

import java.util.List;

public interface DepartmentDao {

    int insert(Department department);

    int update(Department department);

    List<Department> queryAll();

    Department queryById(Integer id);

    boolean isExist(Integer id);

    int deleteById(Integer id);
}
