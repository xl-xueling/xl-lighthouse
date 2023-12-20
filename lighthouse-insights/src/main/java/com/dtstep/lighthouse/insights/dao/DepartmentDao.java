package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Department;

import java.util.List;

public interface DepartmentDao {

    int insert(Department user);

    List<Department> queryAll();

    Department queryById(Integer id);
}
