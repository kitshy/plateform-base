package cn.plateform.api;
import cn.plateform.TestService;
import cn.plateform.mappers.UserMapper;
import cn.plateform.pojo.User;
import cn.plateform.pub.ErrorCodeEnum;
import cn.plateform.pub.aop.AccessPurview;
import cn.plateform.pub.aop.LogPurview;
import cn.plateform.pub.aop.RateLimit;
import cn.plateform.pub.redis.RedisClient;
import cn.plateform.utils.DefaultBackMessage;
import cn.plateform.utils.Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.mchange.v2.lang.ThreadUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    @Autowired
    private UserMapper userMapper;

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


    @RequestMapping(value = "puss")
    @RateLimit(limitNum = 50)
    public String pubss(String name){
         return name;
    }

    @LogPurview
    @RequestMapping(value = "pusss")
    public DefaultBackMessage pubs(){

//        testService.Test();
        
        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
                .eq(User::getUserName,"123")
                .list();
        LambdaQueryWrapper queryWrapper = new QueryWrapper<User>().lambda()
                .eq(User::getUserName,"123");
        IPage<User> iPage = userMapper.selectPage(new Page<>(2,1),queryWrapper);

        List<User> users = userMapper.selectList(queryWrapper);
        DefaultBackMessage message =  DefaultBackMessage.success(iPage.getRecords(), (int) iPage.getTotal());
        return message;
    }

}
