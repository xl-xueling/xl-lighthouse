package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.GroupDto;
import com.dtstep.lighthouse.insights.modal.Group;

import java.util.List;

public interface GroupService {

    int create(Group group);

    List<Group> queryByProjectId(Integer projectId);
}
