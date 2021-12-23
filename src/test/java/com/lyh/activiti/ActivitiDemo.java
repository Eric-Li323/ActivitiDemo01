package com.lyh.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

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

    /**
     * 完成个人任务
     */
    @Test
    public void completTask(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        //3. 根据任务id完成任务
        taskService.complete("2505");
    }

    @Test
    public void completTask2(){
        //1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        //3. 完成任务，参数： 任务id,完成zhangsan的任务
        /*//   获取jerry - myEvection 对应的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("jerry")
                .singleResult();*/

        /*//   获取jeck - myEvection 对应的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("jeck")
                .singleResult();*/

        //   获取rose - myEvection 对应的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("rose")
                .singleResult();

        System.out.println("流程实例id= "+task.getProcessInstanceId());
        System.out.println("任务id= "+task.getId());
        System.out.println("任务负责人= "+task.getAssignee());
        System.out.println("任务名称= "+task.getName());
        //   完成jerry的任务   完成jack的任务   完成rose的任务
        taskService.complete(task.getId());
    }

    /**
     * 使用zip包进行批量的部署
     */
    @Test
    public void deployProcessByZip(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //流程部署
        //读取资源包文件，构建成inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.zip");
        //用inputStream 构建ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //使用压缩包的流进行流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id= "+deploy.getId());
        System.out.println("流程部署的名称= "+deploy.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryPocessDefintion(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //获取当前所有的流程定义
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询当前所有的流程定义,返回流程定义信息的集合
        //processDefinitionKey(流程定义Key)
        //orderByProcessDefinitionVersion 进行排序
        //desc 倒叙
        //list 查询出所有的内容
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //输出信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义ID: "+processDefinition.getId());
            System.out.println("流程定义名称: "+processDefinition.getName());
            System.out.println("流程定义Key: "+processDefinition.getKey());
            System.out.println("流程定义版本: "+processDefinition.getVersion());
            System.out.println("流程部署ID: "+processDefinition.getDeploymentId());
        }
    }

    /**
     * 删除流程部署信息
     *
     * 如果当前的流程并没有完成，想要删除需要特殊方式。原理就是级联删除
     */
    @Test
    public void deleteDeployMent(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过引擎来获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //通过部署id来删除流程部署信息
        String deploymentId = "2501";

        //普通删除方式（当前没有流程正在走）
        //repositoryService.deleteDeployment(deploymentId);

        //级联删除（当前有流程在走）
        repositoryService.deleteDeployment(deploymentId,true);
    }
}
