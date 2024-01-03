package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public interface BaseService {

    Integer getCurrentUserId();

    <T> ListData<T> translateToListData(List<T> list);
}
