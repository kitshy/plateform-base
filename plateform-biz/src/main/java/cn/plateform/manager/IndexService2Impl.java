package cn.plateform.manager;

import cn.plateform.IndexService;
import org.springframework.stereotype.Component;

/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/28
 */
@Component("IndexService2Impl2")
public class IndexService2Impl implements IndexService {
    @Override
    public void test() {
        System.out.println("IndexService2Impl2 ---- name:"+Thread.currentThread().getName()+"  id : "+Thread.currentThread().getId());
        System.out.println("IndexService2Impl");
    }
}
