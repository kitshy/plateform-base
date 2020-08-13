package cn.plateform.pub.redis;

import cn.plateform.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Random;

@Component
@Slf4j
public class RedisLock {

    private static final String REDIS_NAME_SPACE = "REDISLock";

    @Autowired
    private RedisClient redisClient;

    public boolean isLock (String lockName){
        String key = REDIS_NAME_SPACE+lockName;
        String value = (String) redisClient.get(key);
        return !Util.isNullOrEmpty(value);
    }

    public boolean lock(String lockName,long expiredTime){
        String key = REDIS_NAME_SPACE + lockName;

        String value = System.currentTimeMillis() + "_" + RandomUtils.nextLong();
        boolean lockd = redisClient.setNX(key,value,expiredTime);
        if (lockd){
            return true;
        }
        String oldExpiredString = (String) redisClient.get(key);
        long expiredAt = getExpireTime(oldExpiredString,expiredTime);
        // 当前时间大于超时时间，该锁未能自动超时清除
        if (expiredAt<System.currentTimeMillis()){
            String expireTimeInRedis = (String) redisClient.getAndSet(key,value);
            //CAS验证，保证其他线程没有抢占到锁
            if (!oldExpiredString.equalsIgnoreCase(expireTimeInRedis)){
                return false;
            }
        }
        return true;
    }

    /**
     * 释放锁
     * @param lockName
     */
    public void releaseLock(String lockName){
        String key = REDIS_NAME_SPACE+lockName;
        redisClient.del(key);
    }

    private long getExpireTime(String expireTimeStr, long expire){
        long expireTime = 0L;
        if (!StringUtils.isEmpty(expireTimeStr)){
            try {
                expireTime = Long.parseLong(expireTimeStr.split("_")[0]) + expire * 1000;
            }catch (NumberFormatException e){
                log.error("distributed lock value illegal,expireTimeStr={}", expireTimeStr);
            }
        }
        return expireTime;
    }



}
