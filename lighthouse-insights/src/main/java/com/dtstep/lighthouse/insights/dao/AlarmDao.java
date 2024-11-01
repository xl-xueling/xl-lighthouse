package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Alarm;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmDao {

    int insert(Alarm alarm);

    Alarm queryById(Integer id);
}
