package com.lyh.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
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
        String deploymentId = "1";

        //普通删除方式（当前没有流程正在走）
        //repositoryService.deleteDeployment(deploymentId);

        //级联删除（当前有流程在走）
        repositoryService.deleteDeployment(deploymentId,true);
    }

    /**
     * 下载 资源文件
     * 方案1： 使用Acitiviti提供的api下载资源文件，保存到文件目录
     * 方案2： 自己写代码从数据库中下载文件，使用jdbc 读取blob类型、clob类型，保存到文件目录
     * 解决IO操作： commmons-io.jar
     *
     *  此处使用方案1，RepositoryService
     */
    @Test
    public void getDeployment() throws IOException {
        //1. 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取api,RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3. 获取查询对象 ProcessDefinitionQuery， 查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        //4. 通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
        //5. 通过RepositoryService : 传递部署id参数，读取资源信息（png 和 bpmn）
             //5.1 获取png图片的流
             //从流程定义表中，获取png图片的目录和名字
//        String pngName = processDefinition.getDiagramResourceName();
             //通过部署id 和 文件名字来获取图片的资源
//        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
             //5.2 获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        System.out.println("***************************bpmnName: "+bpmnName);
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        //6. 构造OutputStream流
//        File pngFile = new File("d:/evectionflow01.png");
        File bpmnFile = new File("d:/evectionflow01.bpmn");
//        FileOutputStream pngOutputStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        //7. 输入流，输出流的转换
//        IOUtils.copy(pngInput,pngOutputStream);
        IOUtils.copy(bpmnInput,bpmnOutputStream);
        //8. 关闭流
//        pngOutputStream.close();
        bpmnOutputStream.close();
//        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史
     */
    @Test
    public void findHistoryInfo(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        //获取actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        //查询actinst,条件：根据InstanceId
//        instanceQuery.processInstanceId("5001");
        //查询actinst,条件：根据DefinitionId
        instanceQuery.processDefinitionId("myEvection:1:2504");
        //增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        //查询所有内容
        List<HistoricActivityInstance> historicActivityInstances = instanceQuery.list();
        //输出
        for (HistoricActivityInstance hi : historicActivityInstances) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<======================================>");
        }

    }

}
