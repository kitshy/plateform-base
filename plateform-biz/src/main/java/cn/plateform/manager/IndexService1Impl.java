package cn.plateform.manager;

import cn.plateform.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServlet;

/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/28
 */

@Service("IndexService1Impl1")
public class IndexService1Impl extends HttpServlet implements IndexService {
    @Override
    public void test() {
        System.out.println("IndexService1Impl1 ---- name:"+Thread.currentThread().getName()+"  id : "+Thread.currentThread().getId());
        System.out.println("IndexService1Impl");
    }

    @PreDestroy
    public void te(){
        System.out.println("--------------------PreDestroy");
    }

    @PostConstruct
    public void fn(){
        System.out.println("--------------------PostConstruct");
    }

}
