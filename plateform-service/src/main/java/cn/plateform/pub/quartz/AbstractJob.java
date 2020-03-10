package cn.plateform.pub.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


/**
 * 实现 job和runable接口  ，定义为abstarct类型可以作为公共接口实现，用于多个定时任务类继承实现。定时任务Job的父类
 */
@Component
public abstract class AbstractJob implements Job,Runnable {

    public AbstractJob(){}

    @Override
    public void run() {
           try {
               this.execute(null);
           }catch (JobExecutionException jo){
               jo.printStackTrace();
           }
    }

    @Override
    public abstract void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
