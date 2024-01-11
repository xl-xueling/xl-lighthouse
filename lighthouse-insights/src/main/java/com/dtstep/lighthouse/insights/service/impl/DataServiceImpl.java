package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dto.StatisticDataObject;
import com.dtstep.lighthouse.insights.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Override
    public List<StatisticDataObject> dataQuery(Integer statId, long startTime, long endTime, List<String> dimens) {

        return null;
    }

    @Override
    public List<StatisticDataObject> testDataQuery(Integer statId, long startTime, long endTime, List<String> dimens) {

        return null;
    }
}
