package com.lyh.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import pojo.Evection;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试任务完成时添加流程变量
 * @author lyh
 * @date 2021/12/31
 */
public class TestVariables2Complete {

    /**
     * 流程部署
     */
    @Test
    public void testDeployment(){
        //1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3. 使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据中
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables-complete")
                .addClasspathResource("bpmn/evection-global.bpmn20.xml")
                .deploy();
        //4. 输出部署信息
        System.out.println("流程部署id= "+deploy.getId());
        System.out.println("流程部署名字= "+deploy.getName());
    }

    /**
     * 启动流程 的时候设置流程变量
     * 设置流程变量num
     * 设置任务负责人
     */
    @Test
    public void testStartProcess(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //流程定义的key
        String key = "myEvection2";
        //流程变量的map
        Map<String, Object> variables = new HashMap<>();
//        //设置流程变量
//        Evection evection = new Evection();
//        //设置出差日期
//        evection.setNum(3d);
//        //把流程变量的pojo放入map
//        variables.put("evection",evection);
        //设置任务负责人
        variables.put("assignee0","李四3");
        variables.put("assignee0","王经理3");
        variables.put("assignee0","杨总经理3");
        variables.put("assignee0","张财务3");
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, variables);

        System.out.println(processInstance.getId());
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask(){
        //流程定义的key
        String key = "myEvection2";
        //任务负责人
        String assingee = "李四1";
//        String assignee = "王经理1";
//        String assignee = "杨总经理1";
//        String assignee = "张财务1";

        //设置流程变量
        Evection evection = new Evection();
        //设置出差时间
        evection.setNum(2d);
        Map<String,Object> map = new HashMap<>();
        map.put("evection",evection);

        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取taskservice
        TaskService taskService = processEngine.getTaskService();
        //完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assingee)
                .singleResult();
        if(task != null){
            //根据任务id来 完成任务
            taskService.complete(task.getId());
        }
    }

}
