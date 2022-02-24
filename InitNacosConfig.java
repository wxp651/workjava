package com.amway.quickbi.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.amway.quickbi.util.StringQBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.concurrent.Executor;
@Slf4j
@Component
public class InitNacosConfig implements InitializingBean, CommandLineRunner, ApplicationRunner, ApplicationListener<ContextRefreshedEvent> {
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.cloud.nacos.config.file-extension}")
    private String ext;
    public InitNacosConfig() {
        log.info("now is doing construct method");
    }

    //bean初始化后执行
    @PostConstruct
    public void postConstruct() {
        log.info("now is doing @PostConstruct");

    }
    //bean指定initMethod方法，bean初始化后执行
    public void initMethod() {
        log.info("now is doing initMethod");
    }

    //实现ApplicationRunner，bean初始化后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("now is doing afterPropertiesSet");
    }
    //监听SpringBean加载事件，在springboot 初始化完成时执行
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("now is doing something when catch a contextRefreshedEvent");
    }
    //实现CommandLineRunner，SpringBoot启动后后执行
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("now is doing run method of ApplicationRunner Interface");
    }

    //实现CommandLineRunner，SpringBoot启动后后执行
    @Override
    public void run(String... args) throws Exception {
        log.info("now is doing run method of CommandLineRunner Interface");
//        String serverAddr = "localhost";
        String dataId = appName+"."+ext;
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 5000);
        StringQBUtil.transQuickBiInfo(content);
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("recieve:" + configInfo);
                StringQBUtil.transQuickBiInfo(content);
            }

            @Override
            public Executor getExecutor() {
                log.info("getExecutor");
                return null;
            }
        });
    }


}