package cn.plateform;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public void Test(){
        int a = 0;
        if (a==0){
            throw new IllegalArgumentException("参数异常");
        }
    }

}
