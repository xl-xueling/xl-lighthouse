package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.PermissionVO;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.modal.User;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BaseService baseService;

    @Override
    public int create(Permission permission) {
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        permissionDao.insert(permission);
        return permission.getId();
    }

    @Transactional
    @Override
    public int batchCreate(List<Permission> permissionList) {
        int result = 0;
        if(CollectionUtils.isEmpty(permissionList)){
            return result;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Permission> permissions = permissionList.stream().filter(z -> !existPermission(z.getOwnerId(),z.getOwnerType(),z.getRoleId())).map(z -> {
            z.setCreateTime(localDateTime);
            z.setUpdateTime(localDateTime);
            return z;
        }).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(permissions)){
            result = permissionDao.batchInsert(permissions);
        }
        return result;
    }

    @Override
    public boolean checkUserPermission(Integer userId, Integer roleId) {
        return permissionDao.checkUserPermission(userId,roleId);
    }

    @Override
    public List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit) {
        return permissionDao.queryUserPermissionsByRoleId(roleId,limit);
    }

    @Override
    public boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId) {
        return permissionDao.existPermission(ownerId, ownerType, roleId);
    }

    @Transactional
    @Override
    public int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId) {
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        if(existPermission(ownerId,ownerTypeEnum,roleId)){
            return 0;
        }
        if(ownerTypeEnum == OwnerTypeEnum.USER){
            User user = userService.queryById(ownerId);
            Validate.notNull(user);
            Validate.isTrue(user.getState() == UserStateEnum.USER_NORMAL);
        }else if (ownerTypeEnum == OwnerTypeEnum.DEPARTMENT){
            Department department = departmentService.queryById(ownerId);
            Validate.notNull(department);
        }
        Permission permission = new Permission();
        permission.setOwnerId(ownerId);
        permission.setOwnerType(ownerTypeEnum);
        permission.setRoleId(roleId);
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        return permissionDao.insert(permission);
    }

    @Override
    public int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId) {
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        return permissionDao.delete(ownerId, ownerTypeEnum, roleId);
    }

    @Override
    public int releasePermission(Integer id) {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setId(id);
        return permissionDao.delete(queryParam);
    }

    @Override
    public Permission queryById(Integer id) {
        return permissionDao.queryById(id);
    }

    @Override
    public List<Permission> queryUserManagePermission(Integer userId,Integer limit) {
        return permissionDao.queryUserManagePermission(userId,limit);
    }

    @Override
    public int delete(PermissionQueryParam queryParam) {
        return permissionDao.delete(queryParam);
    }

    private PermissionVO translate(Permission permission){
        PermissionVO permissionVO = new PermissionVO(permission);
        if(permissionVO.getOwnerType() == OwnerTypeEnum.USER){
            User user = userService.cacheQueryById(permissionVO.getOwnerId());
            permissionVO.setExtend(user);
        }else if(permissionVO.getOwnerType() == OwnerTypeEnum.DEPARTMENT){
            Department department = departmentService.queryById(permissionVO.getOwnerId());
            permissionVO.setExtend(department);
        }
        return permissionVO;
    }

    @Override
    public ListData<PermissionVO> queryList(PermissionQueryParam queryParam, Integer pageNum, Integer pageSize) {
        ListData<PermissionVO> listData = null;
        PageHelper.startPage(pageNum,pageSize);
        List<PermissionVO> dtoList = new ArrayList<>();
        List<Permission> permissionList = null;
        try{
            permissionList = permissionDao.queryList(queryParam);
        }finally {
            PageHelper.clearPage();
        }
        if(CollectionUtils.isNotEmpty(permissionList)){
            for(Permission permission : permissionList){
                try{
                    PermissionVO dto = translate(permission);
                    dtoList.add(dto);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        return baseService.translateToListData(dtoList);
    }
}
