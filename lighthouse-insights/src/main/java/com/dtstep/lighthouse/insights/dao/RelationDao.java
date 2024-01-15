package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Relation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationDao {

    int batchInsert(List<Relation> list);

    boolean isExist(String hash);
}
