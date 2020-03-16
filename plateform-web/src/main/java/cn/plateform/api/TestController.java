package cn.plateform.api;
import cn.plateform.TestService;
import cn.plateform.pub.aop.AccessPurview;
import cn.plateform.pub.aop.LogPurview;
import cn.plateform.pub.redis.RedisClient;
import cn.plateform.utils.DefaultBackMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class TestController {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TestService testService;

    @RequestMapping(value = "test")
    @LogPurview
    @AccessPurview(repeatCheck = true)
    public String pub(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","songhuayu");
        jsonObject.put("phone",1256182);
        redisClient.set("key",jsonObject);
        Object o = redisClient.get("key");
        JSONObject jsonObject1 = JSON.parseObject(o.toString());
        System.out.println(jsonObject);
        System.out.println(o.toString());
        System.out.println(jsonObject.toString().equals(o.toString()));
        return "123";

    }


//    @LogPurview
    @RequestMapping(value = "puss")
    public String pubss(){
             int i = 1/0;

         return "123456";
    }

    @LogPurview
    @RequestMapping(value = "pusss")
    public DefaultBackMessage pubs(){

//        testService.Test();
        DefaultBackMessage message = DefaultBackMessage.success("成功");
        return message;
    }

}
