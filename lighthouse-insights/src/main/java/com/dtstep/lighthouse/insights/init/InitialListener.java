package com.dtstep.lighthouse.insights.init;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@ComponentScan("com.dtstep.lighthouse")
public class InitialListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitialListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("init start..");
        logger.info("System initialization complete!");
    }
}