package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.RolePair;
import com.dtstep.lighthouse.insights.modal.Resource;
import org.javatuples.Pair;

public interface ResourceService {

    RolePair addResourceCallback(Resource resource);

    void updateResourcePidCallback(Resource resource);

    void deleteResourceCallback(Resource resource);
}
