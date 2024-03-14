package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.insights.vo.GroupVO;

import java.util.List;

public interface GroupService {

    int create(Group group);

    int update(Group group);

    int delete(Group group);

    void changeDebugMode(Group group, SwitchStateEnum switchEnum);

    GroupVO queryById(Integer id) throws Exception;

    Group cacheQueryById(Integer id);

    int count(GroupQueryParam queryParam);

    String getSecretKey(Integer id);

    List<Group> queryByProjectId(Integer projectId);
}
