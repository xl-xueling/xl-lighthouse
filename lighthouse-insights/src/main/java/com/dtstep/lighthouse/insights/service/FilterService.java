package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.RenderFilterConfig;
import com.dtstep.lighthouse.insights.dto.FilterQueryParam;
import com.dtstep.lighthouse.insights.dto.StatFilterConfigParam;

public interface FilterService {

    ListData<RenderFilterConfig> queryDefaultList(String token, Integer pageNum, Integer pageSize) throws Exception;

    ListData<RenderFilterConfig> queryCustomList(FilterQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception;
}
