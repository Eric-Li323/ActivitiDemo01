package com.lyh.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * @author lyh
 * @date 2021/12/20
 */
public class Test01 {

    /**
     * 生成Activiti的相关的表结构
     */
    @Test
    public void test01(){
        // 使用classpath下的activiti.cfg.xml中的配置来创建ProcessEngine对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(engine);
    }

    /**
     * 自定义的方式加载配置文件
     */
    public void test02(){
        //首先创建 ProcessEngineConfiguration 对象
        final ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        //通过ProcessEngineConfiguration 对象来创建 ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();

    }
}
