package cn.plateform;

import cn.plateform.pub.quartz.QuartzManager;
import cn.plateform.system.job.InitScheduledJob;
import cn.plateform.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync 
@EnableRetry
@Slf4j
public class PlateformApplication implements InitializingBean,ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(PlateformApplication.class,args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Util.delayRun(new Runnable() {
            @Override
            public void run() {
                QuartzManager.instance().addJob("plate",InitScheduledJob.class,"0/2 * * * * ?",null);
            }
        },3);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("service start success");
    }
}
