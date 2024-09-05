package com.dtstep.lighthouse.core.test.api;


import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.List;

/**
 * API调用方法示例
 *
 */
public class DataQueryTest {

    static {
        try{
            //修改rpc服务注册中心地址,集群模式为一主一从，默认为部署集群的前两个节点IP,使用逗号分割，单机模式为当前节点IP
            //LightHouse.init("10.206.6.11:4061,10.206.6.12:4061");//集群模式初始化
            LightHouse.init("10.206.6.26:4061");//单机模式初始化
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 对应API：LightHouse.dataQuery(int statId, String secretKey, String dimensValue, long startTime, long endTime)
     * @throws Exception
     *
     * 通过维度查询在指定时间范围内的统计结果
     *
     * 1、对于没有统计维度的统计项，示例：<stat-item title="每分钟_订单金额统计" stat="sum(amount)"/> dimensValue参数传Null；
     * 2、对于单维度的统计项，示例：<stat-item title="每分钟_各省份_订单金额统计" stat="sum(amount)" dimens="province"/> dimensValue参数传province字段对应的某个具体纬度值，如：山东或北京；
     * 3、对于多维度的统计项，示例：<stat-item title="每分钟_各业务线_各省份_订单金额统计" stat="sum(amount)" dimens="biz;province"/> dimensValue参数传biz字段和province字段对应的数值，并使用分号分割，如：家电;山东 或 手机;上海
     */
    @Test
    public void dataQueryTest() throws Exception {
        int statId = 1100594;
        String secretKey = "Kq2Ts5PCBBqTqCFfKtbHekcQObDOZDQMVNuN6Ej5";
        String dimensValue = null;
        long t = System.currentTimeMillis();
        long startTime = DateUtil.getDayStartTime(t);
        long endTime = DateUtil.getDayEndTime(t);
        //statId为对应统计项ID，secretKey为统计项所在统计组的秘钥，dimensValue为纬度值，startTime和endTime为查询起止时间范围
        List<StatValue> statValues = LightHouse.dataQuery(statId,secretKey,dimensValue,startTime,endTime);
        for (StatValue statValue : statValues) {
            //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
            System.out.println("batchTime:" + statValue.getDisplayBatchTime() + ",dimensValue:" + statValue.getDimensValue() + ",value:" + statValue.getValue()
                    + ",statesValue:" + JsonUtil.toJSONString(statValue.getStatesValue()));
        }
        System.out.println("ok!");
    }

    /**
     *
     * 对应API：LightHouse.dataQuery(int statId, String secretKey, String dimensValue, List batchList)
     *
     * batchList为指定具体的批次时间
     *
     */
    public void dataQueryByBatchTest() throws Exception {

    }


}
