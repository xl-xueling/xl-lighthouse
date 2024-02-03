package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.common.modal.FlatTreeNode;
import com.dtstep.lighthouse.common.modal.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao {

    int insert(Project project);

    int update(Project project);

    Project queryById(Integer id);

    int deleteById(Integer id);

    List<Project> queryList(@Param("queryParam")ProjectQueryParam queryParam);

    int countByDepartmentId(Integer departmentId);

    List<FlatTreeNode> queryNodeList(List<Integer> ids);
}
