package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.StatisticDataObject;

import java.util.List;

public interface DataService {

    List<StatisticDataObject> dataQuery(Integer statId,long startTime,long endTime,List<String> dimens);

    List<StatisticDataObject> testDataQuery(Integer statId,long startTime,long endTime,List<String> dimens);
}
