package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationDao {

    int batchInsert(List<Relation> list);

    int insert(Relation relation);

    int update(Relation relation);

    boolean isExist(String hash);

    List<Relation> queryList(Integer subjectId, RelationTypeEnum relationType);

    List<Relation> queryListByPage(@Param("queryParam")RelationQueryParam queryParam);

    List<Relation> queryJoinList(@Param("queryParam")RelationQueryParam queryParam);

    Relation queryById(Integer id);

    int deleteById(Integer id);
}
