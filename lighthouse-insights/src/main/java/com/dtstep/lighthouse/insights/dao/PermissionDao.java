package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao {

    int insert(Permission permission);

    void batchInsert(List<Permission> list);

    int deleteByUserId(Integer userId);

    boolean checkUserPermission(Integer userId, Integer roleId);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int delete(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    List<Permission> queryList(PermissionQueryParam queryParam,Integer pageNum,Integer pageSize);
}
