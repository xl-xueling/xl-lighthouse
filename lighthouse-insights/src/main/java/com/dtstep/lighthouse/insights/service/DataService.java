package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.StatDataObject;

import java.time.LocalDateTime;
import java.util.List;

public interface DataService {

    List<StatDataObject> dataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens);

    List<StatDataObject> testDataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens);
}
