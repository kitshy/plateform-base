package cn.plateform;

import cn.plateform.pub.redis.RedisClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class Test {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @org.junit.jupiter.api.Test
    public void Tests() throws JSONException {

       System.out.println("-==============");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try {
                    System.out.println("The JVM is exit----------------------------------------;1");
                }  catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        System.out.println("-==============");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try {
                    System.out.println("The JVM is exit----------------------------------------;1");
                }  catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
