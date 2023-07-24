package com.dtstep.lighthouse.test;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.test.config.TestConfigContext;
import com.dtstep.lighthouse.test.entity.BehaviorSampleEntity;
import com.dtstep.lighthouse.test.mode.BehaviorModalSample;
import com.dtstep.lighthouse.test.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class LDPOnceTestInstance {

    private static final TestConfigContext testConfigContext = new TestConfigContext();

    private static final long now = System.currentTimeMillis();

    private static final AtomicLong dbIndex = new AtomicLong();

    private static final AtomicLong totalCost = new AtomicLong();

    public static void main(String[] args) throws Exception {
        int dataSize = Integer.parseInt(args[0]);
        LDPConfig.loadConfiguration();
        LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
        BehaviorModalSample modalSample = new BehaviorModalSample();
        String token = modalSample.getToken();
        testConfigContext.setToken(token);
        testConfigContext.setModalSample(modalSample);
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        Validate.notNull(groupExtEntity);
        testConfigContext.setSecretKey(groupExtEntity.getSecretKey());
        createSampleTable(dataSize);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Future<Integer>> futureList = new ArrayList<>();
        for(int i=0;i<8;i++){
            SendThread sendThread = new SendThread();
            Future<Integer> future = executorService.submit(sendThread);
            futureList.add(future);
        }
        int totalSize = 0;
        for(int i=0;i<8;i++){
            Future<Integer> future = futureList.get(i);
            totalSize += future.get();
        }
        System.out.println("total send size:" + totalSize);
        Thread.sleep(10000);
        System.exit(0);
    }

    public static void createSampleTable(int dataSize) throws Exception {
        String token = testConfigContext.getToken();
        String dbName = getDBName();
        int count = DaoHelper.sql.count("SELECT count(1) FROM information_schema.TABLES WHERE table_schema = ? and table_name = ?",dbName,token);
        String tableName = dbName + "." + token;
        if(count == 0){
            String createSql = "CREATE TABLE "+ tableName + "(\n" +
                    "                                  `id` int NOT NULL AUTO_INCREMENT,\n" +
                    "                                  `behavior_id` varchar(80) DEFAULT NULL,\n" +
                    "                                  `request_id` varchar(80) DEFAULT NULL,\n" +
                    "                                  `imei` varchar(80) DEFAULT NULL,\n" +
                    "                                  `province` varchar(80) DEFAULT NULL,\n" +
                    "                                  `city` varchar(80) DEFAULT NULL,\n" +
                    "                                  `behavior_type` int(11) DEFAULT NULL,\n" +
                    "                                  `item_id` int(11) DEFAULT NULL,\n" +
                    "                                  `app_version` varchar(50) DEFAULT NULL,\n" +
                    "                                  `os` varchar(80) DEFAULT NULL,\n" +
                    "                                  `recall_no` int(11) DEFAULT NULL,\n" +
                    "                                  `abtest_no` varchar(50) DEFAULT NULL,\n" +
                    "                                  `top_level` varchar(50) DEFAULT NULL,\n" +
                    "                                  `sec_level` varchar(50) DEFAULT NULL,\n" +
                    "                                  `score` decimal(15,3) DEFAULT NULL,\n" +
                    "                                  `timestamp` datetime DEFAULT NULL,\n" +
                    "                                  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1011 DEFAULT CHARSET=utf8mb3";
            int res = DaoHelper.sql.execute(createSql);
            System.out.printf("create table %s ,result:%s!%n",tableName,res);
        }
        int dbSize = DaoHelper.sql.count(String.format("select count(1) from %s.%s",dbName, testConfigContext.getToken()));
        if(dbSize == 0){
            generateSampleData(dataSize);
        }
    }


    private static String getDBName(){
        String clusterName = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        Validate.notNull(clusterName);
        return String.format("cluster_%s_ldp_mysqldb",clusterName);
    }

    public static void generateSampleData(int size) throws Exception {
        BehaviorModalSample behaviorSampleModal = new BehaviorModalSample();
        String dbName = getDBName();
        DaoHelper.sql.execute(String.format("delete from %s.%s where id > 0",dbName, testConfigContext.getToken()));
        try{
            int count = 0;
            List<BehaviorSampleEntity> behaviorSampleEntityList = new ArrayList<>();
            for(int i=0;i < size;i++){
                BehaviorSampleEntity behaviorSampleEntity = behaviorSampleModal.generateSample();
                behaviorSampleEntityList.add(behaviorSampleEntity);
                if(behaviorSampleEntityList.size() >= 2000){
                    count += behaviorSampleEntityList.size();
                    DaoHelper.sql.insertList(behaviorSampleEntityList);
                    System.out.println("== insert data size:" + count);
                    behaviorSampleEntityList.clear();
                }
            }
            if(behaviorSampleEntityList.size() > 0){
                count += behaviorSampleEntityList.size();
                DaoHelper.sql.insertList(behaviorSampleEntityList);
                System.out.println("== insert data size:" + count);
                behaviorSampleEntityList.clear();
            }
            System.out.println("sample total size is:" + count);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static long sendMessage(long startIndex,int batch) throws Exception{
        int sendSize;
        List<BehaviorSampleEntity> list = DaoHelper.sql.getList(BehaviorSampleEntity.class,"select * from " + testConfigContext.getToken() + " limit ?,?",startIndex,batch);
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }else{
            for (BehaviorSampleEntity sampleEntity : list) {
                Map<String, Object> paramMap = BeanUtil.beanToMap(sampleEntity);
                long t1 = System.currentTimeMillis();
                LightHouse.stat(testConfigContext.getToken(), testConfigContext.getSecretKey(), paramMap, now);
                long t2 = System.currentTimeMillis();
                totalCost.getAndAdd((t2 - t1));
            }
            sendSize = list.size();
        }
        return sendSize;
    }


    public static class SendThread implements Callable<Integer> {

        @Override
        public Integer call() {
            int count = 0;
            try{
                while (true){
                    int batch = 2000;
                    long curIndex = dbIndex.getAndAdd(batch);
                    long sendSize = sendMessage(curIndex,batch);
                    count += sendSize;
                    if(sendSize == 0){
                        break;
                    }else{
                        System.out.println("current thread:" + Thread.currentThread().getName() + ",send size:" + sendSize + ",thread total sent number:" + count);
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return count;
        }
    }
}
