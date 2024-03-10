package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.modal.StatDataObject;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DataService {

    List<String> dimensArrangement(StatExtEntity statExtEntity, LinkedHashMap<String, String[]> dimensParams) throws Exception;

    List<StatDataObject> dataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens) throws Exception;

    List<StatDataObject> testDataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens) throws Exception;
}
