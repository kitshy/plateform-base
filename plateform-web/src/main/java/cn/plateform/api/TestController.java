package cn.plateform.api;
import cn.plateform.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/")
public class TestController {

//    @Autowired
//    private RedisClient redisClient;
//    @Autowired
//    private TestService testService;
//    @Autowired
//    private UserMapper userMapper;
//
//    @RequestMapping(value = "test")
//    @LogPurview
//    @AccessPurview(repeatCheck = true)
//    public String pub(){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name","songhuayu");
//        jsonObject.put("phone",1256182);
//        redisClient.set("key",jsonObject);
//        Object o = redisClient.get("key");
//        JSONObject jsonObject1 = JSON.parseObject(o.toString());
//        System.out.println(jsonObject);
//        System.out.println(o.toString());
//        System.out.println(jsonObject.toString().equals(o.toString()));
//        return "123";
//
//    }
//
//
//    @RequestMapping(value = "puss")
//    @RateLimit(limitNum = 50)
//    public String pubss(String name){
//         return name;
//    }
//
//    @LogPurview
//    @RequestMapping(value = "pusss")
//    public DefaultBackMessage pubs(){
//
////        testService.Test();
//
//        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
//                .eq(User::getUserName,"123")
//                .list();
//        LambdaQueryWrapper queryWrapper = new QueryWrapper<User>().lambda()
//                .eq(User::getUserName,"123");
//        IPage<User> iPage = userMapper.selectPage(new Page<>(2,1),queryWrapper);
//
//        List<User> users = userMapper.selectList(queryWrapper);
//        DefaultBackMessage message =  DefaultBackMessage.success(iPage.getRecords(), (int) iPage.getTotal());
//        return message;
//    }


    @Autowired
    @Qualifier("IndexService1Impl1")
    private IndexService indexService1;

    @Autowired
    @Qualifier("IndexService2Impl2")
    private IndexService indexService2;

    @GetMapping(value = "test")
    public String test(){
        System.out.println("controller ---- name:"+Thread.currentThread().getName()+"  id : "+Thread.currentThread().getId());
        indexService1.test();
        indexService2.test();
        return "test";
    }

    @GetMapping(value = "test1")
    public String test1(){
        System.out.println("controllertest1 ---- name:"+Thread.currentThread().getName()+"  id : "+Thread.currentThread().getId());
        indexService1.test();
        indexService2.test();
        return "test1";
    }

    

}
