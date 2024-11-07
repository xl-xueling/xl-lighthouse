package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmDao {

    int insert(Alarm alarm);

    Alarm queryById(Integer id);

    Alarm queryByParam(@Param("queryParam")AlarmQueryParam queryParam);
}
