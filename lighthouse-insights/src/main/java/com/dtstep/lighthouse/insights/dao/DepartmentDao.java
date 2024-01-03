package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao {

    int insert(Department department);

    int update(Department department);

    List<Department> queryAll();

    Department queryById(Integer id);

    boolean isExist(Integer id);

    int deleteById(Integer id);

    int getLevel(Integer id);

    int getChildLevel(Integer pid);

    int countChildByPid(Integer pid);
}
