package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao {

    int insert(Role role);

    int update(Role role);

    Role queryById(Integer id);

    int deleteById(Integer id);

    void batchInsert(List<Role> list);

    Role queryRole(RoleTypeEnum roleTypeEnum, Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isChildRoleExist(Integer pid);

    List<Role> queryListByPid(Integer pid,Integer pageNum,Integer pageSize);
}
