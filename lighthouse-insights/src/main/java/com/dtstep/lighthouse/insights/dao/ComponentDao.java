package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentDao {

    Integer insert(Component component);
}
