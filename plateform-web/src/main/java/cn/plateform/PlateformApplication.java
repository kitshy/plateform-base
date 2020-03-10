package cn.plateform;

import cn.plateform.pub.quartz.QuartzManager;
import cn.plateform.system.job.InitScheduledJob;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync 
@EnableRetry
public class PlateformApplication implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(PlateformApplication.class,args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        QuartzManager.instance().addJob("plate",InitScheduledJob.class,"0/2 * * * * ?",null);
    }
}
