package cn.plateform.pub.aop;

import cn.plateform.pub.ErrorCodeEnum;
import cn.plateform.pub.exception.BaseException;
import cn.plateform.pub.exception.ReturnException;
import cn.plateform.pub.exception.ServiceException;
import cn.plateform.pub.redis.RedisClient;
import cn.plateform.utils.DefaultBackMessage;
import cn.plateform.utils.Util;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Order(2)
@Aspect
@Component
@Slf4j
public class AccessAopHandle {

    @Autowired
    private RedisClient redisClient;

    @Around(value = "@annotation(cn.plateform.pub.aop.AccessPurview)")
    public Object AuthCheck(ProceedingJoinPoint joinPoint)throws Throwable{
        HttpServletRequest request=null;
        HttpServletResponse response=null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object result=null;

        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
            response = requestAttributes.getResponse();
        }
        if (request!=null&&response!=null){
            Signature sign = joinPoint.getSignature();
            MethodSignature msig=null;
            if (!(sign instanceof MethodSignature)){
                throw new IllegalArgumentException();
            }
            msig=(MethodSignature) sign;
            Object target=joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            AccessPurview accessPurview = currentMethod.getAnnotation(AccessPurview.class);
            if (accessPurview!=null){

                //判断是否符合降级处理,如果符合，流量过大限制访问
                if (accessPurview.demotionLevel()>=10){
                   throw new ReturnException(ErrorCodeEnum.SERVICE_OVERTIME.getCode(),ErrorCodeEnum.SERVICE_OVERTIME.getMessage(),null);
                }
                //判断是否登陆--两种登陆注解都可用，此处用login，根据业务不同使用authType
                if (accessPurview.login()){
                    //登陆业务;  1-根据登陆方式判断是否登陆，2-登陆后业务处理。
                    throw new ReturnException(ErrorCodeEnum.NO_ACCESS.getCode(),ErrorCodeEnum.NO_ACCESS.getMessage(),null);
                }
                //判断是否重复提交,使用redis
                if (accessPurview.repeatCheck()){
                    String key = "REDIS_CHECK_REPEAT_"+currentMethod.getName();
                    JSONObject parms = Util.getJSONExceptBody(request);
                    Object object = redisClient.get(key);
                    if(object!=null){
                        if (parms!=null){
                           if (parms.toString().equals(object.toString())){
                              throw new ReturnException(ErrorCodeEnum.NO_REPEATE.getCode(),ErrorCodeEnum.NO_REPEATE.getMessage(),null);
                           } else {
                               redisClient.set(key,parms,accessPurview.repeatCheckTime());
                           }
                        }
                    }else {
                        if (parms!=null){
                            redisClient.set(key,parms,accessPurview.repeatCheckTime());
                        }
                    }
                }
            }
        }
        long startTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        //接口访问时间
        long endTime = System.currentTimeMillis()-startTime;
        log.info("接口访问时间："+endTime+"--------访问接口URL："+request.getRequestURL());
        if (request!=null&&response!=null){
            try {
                request.getSession().removeAttribute("SESSION_BODY");
            }catch (Exception e){

            }
        }
        return result;
    }

}
