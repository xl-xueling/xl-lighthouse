package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.StatDataObject;

import java.util.List;

public interface DataService {

    List<StatDataObject> dataQuery(Integer statId, long startTime, long endTime, List<String> dimens);

    List<StatDataObject> testDataQuery(Integer statId, long startTime, long endTime, List<String> dimens);
}
