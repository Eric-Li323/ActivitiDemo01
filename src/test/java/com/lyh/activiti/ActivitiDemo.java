package com.lyh.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author lyh
 * @date 2021/12/21
 */
public class ActivitiDemo {

    /**
     * 测试流程部署
     */
    @Test
    public void testDeployment(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.使用service进行流程部署，定义一个流程的名字，把bpmn和png文件部署到数据库里
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn20.xml")
                .addClasspathResource("bpmn/evection.bpmn20.png")
                .deploy();
        // 4.输出部署信息（非必需）
        System.out.println("流程部署id = "+deploy.getId());
        System.out.println("流程部署id = "+deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
        //1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3. 根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        //4. 输出内容
        System.out.println("流程定义ID: "+instance.getProcessDefinitionId());
        System.out.println("流程实例ID: "+instance.getId());
        System.out.println("当前活动的ID: "+instance.getActivityId());
    }

    /**
     * 查询个人待查询的任务
     */
    @Test
    public void testFindPersonalTaskList(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3. 根据流程key 和任务的负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") //流程key
                .taskAssignee("zhangsan")          //要查询的负责人
                .list();
        System.out.println();
        //4. 输出
        for (Task task : taskList) {
            System.out.println("流程实例id= "+task.getProcessInstanceId());
            System.out.println("任务id= "+task.getId());
            System.out.println("任务负责人= "+task.getAssignee());
            System.out.println("任务名称= "+task.getName());
        }
    }

}
