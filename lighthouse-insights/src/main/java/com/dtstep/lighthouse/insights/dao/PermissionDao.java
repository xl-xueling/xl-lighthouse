package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.modal.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao {

    int insert(Permission permission);

    int batchInsert(List<Permission> list);

    Permission queryById(Integer id);

    int delete(@Param("queryParam")PermissionQueryParam queryParam);

    boolean checkUserPermission(Integer userId, Integer roleId);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int delete(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    List<Permission> queryList(@Param("queryParam")PermissionQueryParam queryParam);
}
