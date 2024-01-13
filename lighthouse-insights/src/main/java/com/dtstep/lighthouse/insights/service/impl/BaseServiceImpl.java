package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.github.pagehelper.PageInfo;
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

    @Override
    public <T> ListData<T> translateToListData(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        ListData<T> listData = new ListData<>();
        listData.setList(pageInfo.getList());
        listData.setTotal(pageInfo.getTotal());
        listData.setPageNum(pageInfo.getPageNum());
        listData.setPageSize(pageInfo.getPageSize());
        return listData;
    }
}
