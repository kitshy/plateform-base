package cn.plateform;

import cn.plateform.pub.redis.RedisClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","songhuayu");
        jsonObject.put("phone",1256182);
        redisTemplate.opsForValue().set("key","1111111");

        Object o = redisClient.get("key");
        JSONObject jsonObject1 = JSON.parseObject(o.toString());
        System.out.println(jsonObject.toString().equals(jsonObject1));
    }

}
