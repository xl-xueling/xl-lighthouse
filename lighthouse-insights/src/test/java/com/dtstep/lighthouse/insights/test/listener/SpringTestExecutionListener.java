package com.dtstep.lighthouse.insights.test.listener;

import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class SpringTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        String ldpHome = System.getenv("LDP_HOME");
        if(StringUtil.isEmpty(ldpHome)){
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }else{
            LDPConfig.loadConfiguration();
        }
        System.setProperty("spring.config.name","lighthouse-insights");
        System.setProperty("spring.redis.cluster.nodes", LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER));
        System.setProperty("spring.datasource.driverClassName", LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.driverClassName"));
        System.setProperty("spring.datasource.url", LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionURL"));
        System.setProperty("spring.datasource.username", LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionUserName"));
        System.setProperty("spring.datasource.password", LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionPassword"));
    }
}
