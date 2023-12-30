package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.modal.Permission;

import java.util.List;

public interface PermissionDao {

    int insert(Permission permission);

    List<Permission> queryList(PermissionQueryParam queryParam,Integer pageNum,Integer pageSize);
}
