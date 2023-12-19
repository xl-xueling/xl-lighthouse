package com.dtstep.lighthouse.insights.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.dtstep.lighthouse.insights.dao"})
public class MyBatisConfig {
}
