package cn.plateform;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Retryable(value =RuntimeException.class,maxAttempts = 2)
    public void Test(){
        System.out.println("测试重试机制");
        try {
            int a = 1/0;
        }catch (ArithmeticException e){
            throw new RuntimeException();
        }
    }

}
