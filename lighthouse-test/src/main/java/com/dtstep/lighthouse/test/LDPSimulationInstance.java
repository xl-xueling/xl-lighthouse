package com.dtstep.lighthouse.test;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.test.dao.DaoHelper;
import com.dtstep.lighthouse.test.mode.*;
import org.apache.commons.collections.CollectionUtils;

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
        LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS),LDPConfig.getRunningMode());
        List<Group> groupEntityList = loadTokenList();
        Runnable runnable = () -> {
            try{
                if(CollectionUtils.isNotEmpty(groupEntityList)){
                    for(Group groupEntity:groupEntityList){
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
        service.scheduleAtFixedRate(runnable,0,30, TimeUnit.SECONDS);
    }


    private static class TaskConfig {

        private String token;

        private long timestamp;

        private Group groupEntity;

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

        public Group getGroupEntity() {
            return groupEntity;
        }

        public void setGroupEntity(Group groupEntity) {
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
        }else if("biz_house_price_change_dt".equals(token)){
            sample = new BizHousePriceChangeDTSample();
        }else if("biz_order_stat".equals(token)){
            sample = new BizOrderStatSample();
        }else if("pm_social_privatechat_msg_dt".equals(token)){
            sample = new PMSocialPrivateChatMsgStat();
        }else if("pm_social_prichat_envelope".equals(token)){
            sample = new PMSocialPrichatEnvelopeSample();
        }else if("it_kvdb_request_monitor".equals(token)){
            sample = new ITKVDBRequestMonitorSample();
        }else if("ops_nodes_run_status_monitor".equals(token)){
            sample = new OpsNodesRunStatusMonitorSample();
        }else if("ops_nodes_load_state_dt".equals(token)){
            sample = new OpsNodesLoadStateMonitorSample();
        }else if("ops_nodes_login_state_dt".equals(token)){
            sample = new OpsNodesLoginStateDTSample();
        }else if("pm_feednews_behavior_stat".equals(token)){
            sample = new PMFeedNewsBehaviorStatSample();
        }else if("ai_shortvideo_train_monitor".equals(token)){
            sample = new AiShortVideoTrainMonitorSample();
        }else if("fe_appstart_adv_stat".equals(token)){
            sample = new FeAppStartAdvStatSample();
        }else if("rd_shortvideo_request_stat".equals(token)){
            sample = new RDShortVideoRequestStatSample();
        }else if("rd_shortvideo_recall_size_dt".equals(token)){
            sample = new RDShortVideoRecallSizeSample();
        }else if("rd_shortvideo_recall_cost".equals(token)){
            sample = new RDShortRecallCostSample();
        }else if("om_dau_promote_monitor".equals(token)){
            sample = new OmDauPromoteMonitor();
        }else if("biz_order_conversion_rate".equals(token)){
            sample = new BizOrderConversionRateSample();
        }
        if(sample == null){
            return;
        }
        int onceSize = 50 + ThreadLocalRandom.current().nextInt(10);
        for(int i=0;i<onceSize;i++){
            HashMap<String,Object> paramMap = sample.generateSample();
            LightHouse.stat(token,task.getGroupEntity().getSecretKey(),paramMap,time);
        }
        System.out.println("send success,batch:"+DateUtil.formatTimeStamp(task.getTimestamp(),"yyyy-MM-dd HH:mm:ss") + ",token: " + token + ",size:" + onceSize);
    }

    public static List<Group> loadTokenList() throws Exception {
        String clusterId = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        List<Group> list = DaoHelper.sql.getList(Group.class,"SELECT token,secret_key FROM cluster_"+clusterId+"_ldp_cmdb.ldp_stat_group");
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
    }

    private void initSimulationConfig(){

    }
}
