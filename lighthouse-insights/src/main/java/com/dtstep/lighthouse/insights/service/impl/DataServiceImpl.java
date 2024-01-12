package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dto.StatDataObject;
import com.dtstep.lighthouse.insights.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Override
    public List<StatDataObject> dataQuery(Integer statId, long startTime, long endTime, List<String> dimens) {

        return null;
    }

    @Override
    public List<StatDataObject> testDataQuery(Integer statId, long startTime, long endTime, List<String> dimens) {

        return null;
    }
}
