package com.dtstep.lighthouse.insights.service.impl;

import com.clearspring.analytics.util.Lists;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseServiceImpl implements BaseService {

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? -1 : (Integer) authentication.getPrincipal();
    }

    @Deprecated
    @Override
    public <T> ListData<T> translateToListData(List<T> list) {
        if(CollectionUtils.isEmpty(list)){
            PageInfo<T> pageInfo = new PageInfo<>(list);
            ListData<T> listData = new ListData<>();
            listData.setList(Lists.newArrayList());
            listData.setTotal(0);
            listData.setPageNum(0);
            listData.setPageSize(0);
            return listData;
        }else{
            PageInfo<T> pageInfo = new PageInfo<>(list);
            ListData<T> listData = new ListData<>();
            listData.setList(pageInfo.getList());
            listData.setTotal(pageInfo.getTotal());
            listData.setPageNum(pageInfo.getPageNum());
            listData.setPageSize(pageInfo.getPageSize());
            return listData;
        }
    }

}
