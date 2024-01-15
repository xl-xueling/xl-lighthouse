package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.MetricSetDao;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricSetServiceImpl implements MetricSetService {

    @Autowired
    private MetricSetDao metricSetDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Transactional
    @Override
    public int create(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setCreateTime(localDateTime);
        metricSet.setUpdateTime(localDateTime);
        metricSetDao.insert(metricSet);
        int id = metricSet.getId();
        RolePair rolePair = resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Metric,id,0));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId,OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        return id;
    }


    @Override
    public int grantAccessPermissions(Integer id, List<Integer> initUsersPermission,List<Integer> initDepartmentsPermission) {
        MetricSet metricSet = metricSetDao.queryById(id);
        Role role = roleService.cacheQueryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,id);
        List<Permission> permissionList = new ArrayList<>();
        if(metricSet.getPrivateType() == PrivateTypeEnum.Private && CollectionUtils.isNotEmpty(initDepartmentsPermission)){
            for(int i=0;i<initDepartmentsPermission.size();i++){
                Integer tempDepartmentId = initDepartmentsPermission.get(i);
                Permission tempPermission = new Permission(tempDepartmentId,OwnerTypeEnum.DEPARTMENT,role.getId());
                permissionList.add(tempPermission);
            }
        }
        if(metricSet.getPrivateType() == PrivateTypeEnum.Private && CollectionUtils.isNotEmpty(initUsersPermission)){
            for(int i=0;i<initUsersPermission.size();i++){
                Integer userId = initUsersPermission.get(i);
                Permission tempPermission = new Permission(userId,OwnerTypeEnum.USER, role.getId());
                permissionList.add(tempPermission);
            }
        }
        return permissionService.batchCreate(permissionList);
    }

    @Override
    public int update(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setUpdateTime(localDateTime);
        return metricSetDao.update(metricSet);
    }

    private MetricSetDto translate(MetricSet metricSet){
        MetricSetDto metricSetDto = new MetricSetDto(metricSet);
        Role role = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,metricSet.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(role.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            metricSetDto.setAdmins(admins);
        }
        return metricSetDto;
    }

    @Override
    public MetricSetDto queryById(Integer id) {
        MetricSet metricSet = metricSetDao.queryById(id);
        return translate(metricSet);
    }

    @Override
    public int binded(MetricBindParam bindParam) {

        return 0;
    }

    @Override
    public ListData<MetricSet> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<MetricSet> metricSetListData = null;
        try{
            List<MetricSet> metricSetList = metricSetDao.queryList(queryParam);
            metricSetListData = baseService.translateToListData(metricSetList);
        }finally {
            PageHelper.clearPage();
        }
        return metricSetListData;
    }
}
