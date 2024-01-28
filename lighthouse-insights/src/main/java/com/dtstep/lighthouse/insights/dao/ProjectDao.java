package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.FlatTreeNode;
import com.dtstep.lighthouse.insights.modal.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao {

    int insert(Project project);

    int update(Project project);

    Project queryById(Integer id);

    int deleteById(Integer id);

    List<Project> queryList(ProjectQueryParam queryParam,Integer pageNum,Integer pageSize);

    int countByDepartmentId(Integer departmentId);

    List<FlatTreeNode> queryNodeList(List<Integer> ids);
}
