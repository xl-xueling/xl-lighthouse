package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.modal.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao {

    int insert(Group group);

    int update(Group group);

    Group queryById(Integer id);

    String getSecretKey(Integer id);

    List<Group> queryByProjectId(Integer projectId);

    int count(@Param("queryParam")GroupQueryParam queryParam);
}
