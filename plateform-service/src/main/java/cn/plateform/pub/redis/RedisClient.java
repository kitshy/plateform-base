package cn.plateform.pub.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisClient{

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean setNX(final String key,final String value,final long expire){
        return (boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection){
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] newExpireTimeBytes = redisTemplate.getStringSerializer().serialize(value);
                boolean locked = false;
                try {
                    if (redisConnection.setNX(keyBytes, newExpireTimeBytes)) {
                        // setNX成功
                        locked = true;
                    } else {

                    }
                    if (locked) {
                        redisConnection.expire(keyBytes, expire);
                    }
                } catch (Exception e) {
                    log.warn("get distributed lock exception[key={}]:", key, e);
                    locked = false;
                }
                return locked;
            }
        });
    }


    /**
     * 指定缓存失效时间
     * @param key
     * @param time  时间为秒（s）
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if (time>0){
                redisTemplate.expire(key,time,TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     * @param key
     * @return
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public  boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存，可以一个或者多个
     * @param key
     */
    public void del(String ... key){
        if (key.length==1){
            redisTemplate.delete(key);
        }else {
            redisTemplate.delete(CollectionUtils.arrayToList(key));
        }
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @return
     */
    public boolean append(String key, String value){
        try {
            redisTemplate.opsForValue().append(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加缓存并且设置过期时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key,Object value,long time){
        try {
            if (time>0){
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
            }else {
                set(key,value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *重置缓存数据并且获取旧值
     * @param key
     * @param value
     * @return
     */
    public Object getAndSet(String key,Object value){
        try {
            return redisTemplate.opsForValue().getAndSet(key,value);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 递增
     * @param key
     * @param dalta
     * @return
     */
    public long incr(String key,long dalta){
        if (dalta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,dalta);
    }

    /**
     * 递减
     * @param key
     * @param dalta
     * @return
     */
    public long decr(String key,long dalta){
        if (dalta<0){
            throw  new RuntimeException("递增因子必须大于0") ;
        }
        return redisTemplate.opsForValue().decrement(key,dalta);
    }



}
