package com.lyh.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * @author lyh
 * @date 2021/12/24
 */
public class ActivitiBusinessDemo {

    /**
     * 添加业务Key 到Activiti的表
     */
    @Test
    public void addBusinessKey(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3. 启动流程的过程中，添加businesskey
             //第一个参数： 流程定义的Key
             //第二个参数： businessKey,有出差申请的的id
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        //4. 输出打印内容
        System.out.println("businessKey: "+instance.getBusinessKey());
    }

    /**
     * 全部流程实例的挂起和激活
     * suspend 暂停
     */
    @Test
    public void suspendAllProcessInstance(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3. 查询流程定义的信息,获取流程定义的查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        //4. 获取当前流程定义的实例是否都挂起状态
        boolean suspended = processDefinition.isSuspended();
        //5. 获取流程定义的id
        String definitionId = processDefinition.getId();
        //6. 如果是挂起状态，改为激活  (如果是激活状态，改为挂起状态)
        if(suspended){
            //如果是挂起，可以执行激活的操作, 参数1: 流程定义id, 参数2：是否激活， 参数3： 激活时间
            repositoryService.activateProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id: "+definitionId+ ", 已激活");
        }else {
            //如果是激活状态，改为挂起状态， 参数1：流程定义id, 参数2：是否暂停 参数3： 暂停时间
            repositoryService.suspendProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id: "+definitionId+ ", 已挂起");
        }
    }

    /**
     * 挂起，激活单个流程实例
     */
    @Test
    public void suspendSingleProcessInstance(){
        //1. 获取流程引擎、
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3. 通过RuntimeService获取实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("7501")
                .singleResult();
        //4. 得到当前流程实例的暂停状态
        boolean suspended = instance.isSuspended();
        //5. 获取流程实例id
        String instanceId = instance.getId();
        //6. 判断是否已经暂停，如果已经暂停，就执行激活操作 (如果激活，就执行暂停操作)
        if(suspended){
            //如果已经暂停,就执行激活
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id: "+instanceId+"已经激活");
        }else {
            //如果已经激活，就执行暂停
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id: "+instanceId+"已经暂停");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        //3. 使用TaskService获取任务,参数为流程实例的id,负责人
        Task task = taskService.createTaskQuery()
                .processInstanceId("7501")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println("流程实例id= "+task.getProcessInstanceId());
        System.out.println("流程任务id= "+task.getId());
        System.out.println("负责人id= "+task.getAssignee());
        System.out.println("流程实例id= "+task.getProcessInstanceId());
        //4. 根据任务id完成任务
        taskService.complete(task.getId());
    }
}
