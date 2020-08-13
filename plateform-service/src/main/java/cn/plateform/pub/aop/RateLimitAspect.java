package cn.plateform.pub.aop;

import cn.plateform.pub.ErrorCodeEnum;
import cn.plateform.pub.exception.ReturnException;
import cn.plateform.pub.exception.ServiceException;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 说明
 *
 * @Description : 使用guava的限流工具RateLimiter，底层原理实现是令牌桶法
 * @Author : songHuaYu
 * @Date: 2020/08/12
 */
@Aspect
@Order(1)
@Slf4j
@Component
public class RateLimitAspect {

    private static ConcurrentHashMap<String,RateLimiter> rateLimiterMap=new ConcurrentHashMap<String,RateLimiter>();
    private RateLimiter rateLimiter;
    private HttpServletResponse response;

    @Around(value = "@annotation(cn.plateform.pub.aop.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint)throws NoSuchMethodException{

        Object object=null;
        //获取被代理对象的属性名称集合
        Signature signature = joinPoint.getSignature();
        //获取拦截的方法名称
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取被织入增加处理目标对象
        Object target = joinPoint.getTarget();

        Method currentMethod = target.getClass().getMethod(methodSignature.getName(),methodSignature.getParameterTypes());
        //获取注解信息
        RateLimit rateLimit = currentMethod.getAnnotation(RateLimit.class);

//-------------------------------------todo        此处限流key-value处理方法不同项目需要优化
        //获取注解每秒加入桶中的token
        int limitNum = (rateLimit==null?50:rateLimit.limitNum());
        //注解所在方法名区分不同的限流策略
        String funcName = methodSignature.getName();

        if (rateLimiterMap.containsKey(funcName)){
            rateLimiter = rateLimiterMap.get(funcName);
        }else {
            rateLimiterMap.put(funcName,RateLimiter.create(limitNum));
            rateLimiter = rateLimiterMap.get(funcName);
        }

        if (rateLimiter.tryAcquire()){
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("joinPoint error ",throwable.toString());
                throw new ServiceException();
            }
        }else {
            throw new ReturnException(ErrorCodeEnum.SERVICE_OVERTIME.getCode(),ErrorCodeEnum.SERVICE_OVERTIME.getMessage(),null);
        }
        
    }

}
