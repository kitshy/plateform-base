package cn.plateform.pub.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class QuartzManager {

    private static String JOB_GROUP_NAME="Quartz_JOBGROUP";
    private static String TRIGGER_GROUP_NAME = "Quartz_TRIGGERGROUP";
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private static QuartzManager quartzManager = new QuartzManager();

    public static QuartzManager instance(){
        return quartzManager;
    }

    public QuartzManager(){}

    /**
     * 添加一个定时任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     * @param jobClass
     * @param time
     * @param tag
     * @param <T>
     */
    public <T extends AbstractJob> void addJob(String jobName, Class<T> jobClass, String time, Object tag){
        addJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, jobClass, time, tag);
    }


    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     */
    public void removeJob(String jobName){
        removeJobByJobAndTrigger(jobName,JOB_GROUP_NAME,TRIGGER_GROUP_NAME);
    }


    /**
     * 添加 date 时间格式定时任务 (使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     * @param jobClass
     * @param startTime
     * @param tag
     * @param <T>
     */
    public <T extends AbstractJob> void  addSimpleJob(String jobName, Class<T>  jobClass, Date startTime, Object tag) {
        try {
            Scheduler scheduler =schedulerFactory .getScheduler();
            JobDataMap map = new JobDataMap();
            map.put("tag", tag);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).usingJobData(map) .build();// new JobDetailImpl (jobName, JOB_GROUP_NAME, (Class<? extends Job>) Class.forName(jobClass));// 任务名，任务组，任务执行类
            // 触发器

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,TRIGGER_GROUP_NAME).startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0)).build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个定时任务
     * @param jobName           任务名
     * @param jobGroupName     任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param tClass            任务
     * @param time              时间设置，表达式
     * @param tag               附加数据
     * @param <T>
     */
    public <T extends AbstractJob> void addJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName,Class<T> tClass,String time,Object tag){
        try{
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("tag",tag);
            //job  调度工作类     // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(tClass).withIdentity(jobName,jobGroupName).usingJobData(jobDataMap).build();
            //      调度触发器
            CronTrigger trigger = newTrigger().withIdentity(triggerName,triggerGroupName).withSchedule(cronSchedule(time)).build();
            //  调度工厂类
            scheduler.scheduleJob(jobDetail,trigger);
           if (!scheduler.isShutdown()){
               // 启动
               scheduler.start();
           }
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }



    /**
     * 修改一个任务的触发时间 ，    (使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     * @param time
     */
    public void modifyJobTimeByJob(String jobName,String jobGroupName,String time){
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(jobName,TRIGGER_GROUP_NAME));
            if (trigger==null){
                return;
            }
            String oldTime=trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)){
                JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName, jobGroupName));// .getJobDetail(jobName, JOB_GROUP_NAME);
                Class objJobClass = jobDetail.getJobClass();
                Object tag = jobDetail.getJobDataMap().get("tag");
                String jobClass = objJobClass.getName();
                log.info("修改了任务：" + jobName + "；GroupName：" + jobGroupName + "；jobName：" + jobName + "；time：" + time);
                removeJob(jobName);
                addJob(jobName, objJobClass, time, tag);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * 修改一个任务的触发时间
     * @param triggerName
     * @param triggerGroupName
     * @param time
     */
    public void modifyJobTimeByTrigger(String triggerName,String triggerGroupName,String time){
       try {
           Scheduler scheduler = schedulerFactory.getScheduler();
           CronTrigger trigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(triggerName, triggerGroupName));
           if (trigger==null){
               return;
           }
           String oldTime = trigger.getCronExpression();
           if (!oldTime.equalsIgnoreCase(time)){
               CronTrigger cronTrigger = trigger;
               // 修改时间
               ((CronTriggerImpl) cronTrigger).setCronExpression(time);
               // 重启触发器
               scheduler.resumeTrigger(new TriggerKey(triggerName, triggerGroupName));
           }
       } catch (Exception e){
           e.printStackTrace();
       }
    }


    /**
     * 移除一个任务 方式一
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerGroupName
     */
    public void removeJobByJobAndTrigger(String jobName, String jobGroupName,String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(new TriggerKey(jobName, triggerGroupName));// 停止触发器
            scheduler.unscheduleJob(new TriggerKey(jobName, triggerGroupName));// 移除触发器
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
            log.info("删除了任务：" + jobName + "；GroupName：" + jobGroupName + "；jobName：" + jobName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除一个任务  方式二
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public void removeJobByJobAndTrigger(String jobName, String jobGroupName,
                          String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器
            scheduler.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
            log.info("删除了任务：" + jobName + "；GroupName：" + jobGroupName + "；jobName：" + jobName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 启动所有定时任务
     */
    public void startJob(){
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭所有定时任务
     */
    public void shundownJobs(){
       try {
           Scheduler scheduler = schedulerFactory.getScheduler();
           scheduler.shutdown();
       }catch (Exception e){
           e.printStackTrace();
       }
    }


}
