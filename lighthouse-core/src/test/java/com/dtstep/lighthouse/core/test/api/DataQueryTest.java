package com.dtstep.lighthouse.core.test.api;


import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Java API调用示例
 */
public class DataQueryTest {

    static {
        try{
            //修改rpc服务注册中心地址,集群模式为一主一从，默认为部署集群的前两个节点IP,使用逗号分割，单机模式为当前节点IP
            //LightHouse.init("10.206.6.11:4061,10.206.6.12:4061");//集群模式初始化
            LightHouse.init("10.206.6.31:4061");//单机模式初始化
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private final String callerName = "caller:lighthouse_test_call";

    private final String callerKey = "ssadsgasdg";

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
        int statId = 1100607;
        String dimensValue = null;
        long t = System.currentTimeMillis();
        long startTime = DateUtil.getDayStartTime(t);
        long endTime = DateUtil.getDayEndTime(t);
        //statId为对应统计项ID，secretKey为统计项所在统计组的秘钥，dimensValue为纬度值，startTime和endTime为查询起止时间范围
        List<StatValue> statValues = LightHouse.dataQuery(callerName,callerKey,statId,dimensValue,startTime,endTime);
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
     * 1、batchList为指定具体的批次时间列表；
     * 2、dimensValue传值逻辑与上述示例相同；
     *
     */
    @Test
    public void dataQueryByBatchTest() throws Exception {
        int statId = 1100602;
        String secretKey = "Kq2Ts5PCBBqTqCFfKtbHekcQObDOZDQMVNuN6Ej5";
        String dimensValue = "北京市";
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
        //statId为对应统计项ID，secretKey为统计项所在统计组的秘钥，dimensValue为纬度值，batchList为批次时间列表
        List<StatValue> statValues = LightHouse.dataQuery(callerName,callerKey,statId,dimensValue,batchList);
        for (StatValue statValue : statValues) {
            //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
            System.out.println("batchTime:" + statValue.getDisplayBatchTime() + ",dimensValue:" + statValue.getDimensValue() + ",value:" + statValue.getValue()
                    + ",statesValue:" + JsonUtil.toJSONString(statValue.getStatesValue()));
        }
        System.out.println("ok!");
    }

    /**
     * 对应API：LightHouse.dataQueryWithDimensList(int statId, String secretKey, List dimensValueList, long startTime,long endTime)
     * 查询多个维度信息指定时间范围的统计结果,dimensValueList为多个维度的集合，每个维度取值逻辑与上述示例一致
     *
     * @throws Exception
     */
    @Test
    public void dataQueryWithDimensListTest() throws Exception {
        int statId = 1100601;
        String secretKey = "Kq2Ts5PCBBqTqCFfKtbHekcQObDOZDQMVNuN6Ej5";
        List<String> dimensValueList = new ArrayList<>();
        dimensValueList.add("山东省");
        dimensValueList.add("四川省");
        dimensValueList.add("湖南省");
        dimensValueList.add("辽宁省");
        dimensValueList.add("贵州省");
        long t = System.currentTimeMillis();
        long startTime = DateUtil.getDayStartTime(t);
        long endTime = DateUtil.getDayEndTime(t);
        Map<String,List<StatValue>> resultMap = LightHouse.dataQueryWithDimensList(callerName,callerKey,statId,dimensValueList,startTime,endTime);
        for(String dimensValue : resultMap.keySet()){
            List<StatValue> statValues = resultMap.get(dimensValue);
            for (StatValue statValue : statValues) {
                //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
                System.out.println("dimensValue:" + dimensValue + ",batchTime:" + statValue.getDisplayBatchTime() + ",value:" + statValue.getValue()
                        + ",statesValue:" + JsonUtil.toJSONString(statValue.getStatesValue()));
            }
        }
    }

    /**
     * 对应API：LightHouse.dataQueryWithDimensList(int statId, String secretKey, List dimensValueList, List batchList)
     * 查询多个维度信息指定批次的统计结果,dimensValueList为多个维度的集合，每个维度取值逻辑与上述示例一致，batchList为批次时间列表
     * @throws Exception
     */
    @Test
    public void dataQueryWithDimensListByBatchTest() throws Exception {
        int statId = 1100597;
        String secretKey = "Kq2Ts5PCBBqTqCFfKtbHekcQObDOZDQMVNuN6Ej5";
        List<String> dimensValueList = new ArrayList<>();
        dimensValueList.add("山东省;1");
        dimensValueList.add("山东省;2");
        dimensValueList.add("四川省;1");
        dimensValueList.add("四川省;2");
        dimensValueList.add("湖南省;2");
        dimensValueList.add("辽宁省;1");
        dimensValueList.add("贵州省;1");
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
        Map<String,List<StatValue>> resultMap = LightHouse.dataQueryWithDimensList(callerName,callerKey,statId,dimensValueList,batchList);
        for(String dimensValue : resultMap.keySet()){
            List<StatValue> statValues = resultMap.get(dimensValue);
            for (StatValue statValue : statValues) {
                //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
                System.out.println("dimensValue:" + dimensValue + ",batchTime:" + statValue.getDisplayBatchTime() + ",value:" + statValue.getValue()
                        + ",statesValue:" + JsonUtil.toJSONString(statValue.getStatesValue()));
            }
        }
    }


    /**
     *
     * 该接口需要v2.2.7以上版本支持！
     *
     * 对应API：LightHouse.limitQuery(int statId, String secretKey, Long batchTime)
     * 查询Limit同结果数据，batchTime为对应的批次时间
     *
     * 该接口需要v2.2.7以上版本支持！
     *
     * @throws Exception
     */
    @Test
    public void testLimitQuery() throws Exception {
        int statId = 1100601;
        String secretKey = "Kq2Ts5PCBBqTqCFfKtbHekcQObDOZDQMVNuN6Ej5";
        long batchTime = DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss");
        List<LimitValue> limitValues = LightHouse.limitQuery(callerName,callerKey,statId,batchTime);
        for(LimitValue limitValue : limitValues){
            System.out.println("dimensValue:" + limitValue.getDimensValue() + ",score:" + limitValue.getScore());
        }
    }



}
