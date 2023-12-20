package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Group;

public interface GroupDao {

    int insert(Group group);

    Group queryById(Integer id);
}
