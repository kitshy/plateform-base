package cn.plateform.system.job;

import cn.plateform.pub.quartz.AbstractJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@DisallowConcurrentExecution  //todo --注意此注解。。 告诉Quartz不要并发地执行同一个job定义（这里指特定的job类）的多个实例。
public class InitScheduledJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //初始化需要定时的业务，从数据库获取，相关任务组job
          log.info("初始化----------");
    }
}
