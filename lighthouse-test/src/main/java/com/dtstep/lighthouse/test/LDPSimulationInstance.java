package com.dtstep.lighthouse.test;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.test.mode.ModalSample;
import com.dtstep.lighthouse.test.mode.OmAppStartDauStatSample;
import com.dtstep.lighthouse.test.mode.SimulationModalSample;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class LDPSimulationInstance {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(6);

    private static final Queue<TaskConfig> tasksQueue = new LinkedBlockingDeque<>();

    static {
        for(int i=0;i<5;i++){
            service.scheduleWithFixedDelay(new ProcessThread(),0,5, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        LDPConfig.loadConfiguration();
        LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
        List<GroupEntity> groupEntityList = loadTokenList();
        Runnable runnable = () -> {
            try{
                if(CollectionUtils.isNotEmpty(groupEntityList)){
                    for(GroupEntity groupEntity:groupEntityList){
                        long batch = DateUtil.batchTime(1,TimeUnit.MINUTES,System.currentTimeMillis());
                        TaskConfig taskConfig = new TaskConfig();
                        taskConfig.setToken(groupEntity.getToken());
                        taskConfig.setGroupEntity(groupEntity);
                        taskConfig.setTimestamp(batch);
                        tasksQueue.add(taskConfig);
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        };
        service.scheduleAtFixedRate(runnable,0,1, TimeUnit.MINUTES);
    }


    private static class TaskConfig {

        private String token;

        private long timestamp;

        private GroupEntity groupEntity;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public GroupEntity getGroupEntity() {
            return groupEntity;
        }

        public void setGroupEntity(GroupEntity groupEntity) {
            this.groupEntity = groupEntity;
        }
    }


    public static class ProcessThread extends Thread {

        @Override
        public void run() {
            TaskConfig msg = tasksQueue.poll();
            if(msg == null){
                return;
            }
            try{
                process(msg);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void process(TaskConfig task) throws Exception {
        String token = task.getToken();
        long time = task.getTimestamp();
        SimulationModalSample<HashMap<String,Object>> sample = null;
        if("om_appstart_dau_stat".equals(token)){
            sample = new OmAppStartDauStatSample();
        }
        if(sample == null){
            return;
        }
        int onceSize = 100 + ThreadLocalRandom.current().nextInt(15);
        for(int i=0;i<onceSize;i++){
            HashMap<String,Object> paramMap = sample.generateSample();
            LightHouse.stat(token,task.getGroupEntity().getSecretKey(),paramMap,time);
        }
    }

    public static List<GroupEntity> loadTokenList() throws Exception {
        String clusterId = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        List<GroupEntity> list = DaoHelper.sql.getList(GroupEntity.class,"SELECT token,secret_key FROM cluster_"+clusterId+"_ldp_mysqldb.ldp_stat_group");
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
    }

    private void initSimulationConfig(){

    }
}
