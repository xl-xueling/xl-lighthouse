package com.dtstep.lighthouse.core.test.builtin;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class BuiltinLoaderTest extends CoreBaseTest {

    @Test
    public void testLoadGroup() throws Exception {
        String s = RandomID.id(32);
        System.out.println(s);
        List<GroupExtEntity> groupExtEntityList = BuiltinLoader.getAllGroups();
        for(int i=0;i<groupExtEntityList.size();i++){
            System.out.println("group:" + JsonUtil.toJSONString(groupExtEntityList.get(i)));
            List<StatExtEntity> statExtEntityList = BuiltinLoader.getBuiltinStatByGroupId(groupExtEntityList.get(i).getId());
            for(int n=0;n<statExtEntityList.size();n++){
                StatExtEntity statExtEntity = statExtEntityList.get(n);
                System.out.println("stat:" + JsonUtil.toJSONString(statExtEntity));
            }
        }

    }
}
