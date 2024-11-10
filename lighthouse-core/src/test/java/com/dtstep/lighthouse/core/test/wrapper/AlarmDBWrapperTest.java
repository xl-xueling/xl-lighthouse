package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.entity.AlarmExtEntity;
import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmChannel;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.plugins.PluginManager;
import com.dtstep.lighthouse.core.plugins.StatAlarmPlugin;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.AlarmDBWrapper;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

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

    @Test
    public void testPluginManager() throws Exception {
        Optional<StatAlarmPlugin> s  =  PluginManager.getAlarmPlugin();
        System.out.println("s is:" + s.isPresent());
    }

    @Test
    public void testUpdate() throws Exception {
        String s = "{\"id\":39,\"title\":\"asdagasg22\",\"uniqueCode\":\"default.gEXMbC\",\"divide\":false,\"resourceId\":1100617,\"resourceType\":6,\"state\":false,\"match\":1,\"conditions\":[{\"key\":\"Condition-uvFzV\",\"indicator\":0,\"last\":1,\"compare\":\"GE\",\"divide\":false,\"overall\":{\"threshold\":0,\"state\":true},\"p0\":{\"threshold\":0,\"state\":false},\"p1\":{\"threshold\":0,\"state\":false},\"p2\":{\"threshold\":0,\"state\":false},\"p3\":{\"threshold\":0,\"state\":false}}],\"templateId\":0,\"silent\":300,\"recover\":false,\"delay\":60,\"desc\":\"asdgag\",\"dimens\":\"asdasgd\",\"createTime\":1730978049000,\"updateTime\":1730978049000}";
        Alarm alarm = JsonUtil.toJavaObject(s,Alarm.class);
    }

    @Test
    public void testQueryAlarmChannel() throws Exception {
        AlarmChannel alarmChannel = AlarmDBWrapper.queryAlarmChannel();
        System.out.println("alarmChannel is:" + JsonUtil.toJSONString(alarmChannel));
    }
}
