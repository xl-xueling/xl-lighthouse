package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.MetricSetDao;
import com.dtstep.lighthouse.insights.dto.MetricSetDto;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public int create(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setCreateTime(localDateTime);
        metricSet.setUpdateTime(localDateTime);
        return metricSetDao.insert(metricSet);
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
