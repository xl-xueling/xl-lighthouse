package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmDao {

    int insert(Alarm alarm);

    int update(Alarm alarm);

    int deleteById(Integer id);

    Alarm queryById(Integer id);

    Alarm queryByParam(@Param("queryParam")AlarmQueryParam queryParam);

    int countByParam(@Param("queryParam")AlarmQueryParam queryParam);

    List<Alarm> queryList(@Param("queryParam")AlarmQueryParam queryParam);
}
