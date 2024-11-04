package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.entity.AlarmExtEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.AlarmDBWrapper;
import org.junit.Test;

import java.util.List;

public class AlarmDBWrapperTest extends CoreBaseTest {

    @Test
    public void testQueryById() throws Exception {
        int id = 24;
        AlarmExtEntity alarmExtEntity = AlarmDBWrapper.queryById(id);
        System.out.println("alarmExtEntity is:" + JsonUtil.toJSONString(alarmExtEntity));
    }

    @Test
    public void testQueryByStatId() throws Exception {
        int statId = 1100619;
        List<AlarmExtEntity> alarmExtEntityList = AlarmDBWrapper.queryByStatId(statId);
        for(AlarmExtEntity alarmExtEntity : alarmExtEntityList){
            System.out.println("alarmExtEntity is:" + JsonUtil.toJSONString(alarmExtEntity));
        }
    }
}
