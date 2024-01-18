package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.insights.modal.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDao {

    int insert(Record record);

    List<Record> queryList(@Param("queryParam")RecordQueryParam queryParam);
}
