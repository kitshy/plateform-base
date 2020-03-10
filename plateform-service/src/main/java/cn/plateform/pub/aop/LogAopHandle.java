package cn.plateform.pub.aop;

import cn.plateform.pub.exception.ServiceException;
import cn.plateform.utils.DefaultBackMessage;
import cn.plateform.utils.Util;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
public class LogAopHandle {


    //正常返回时走的路径，包括已处理抛出的异常都走这里  // object这个就是方法返回值
    @AfterReturning(value = "@annotation(cn.plateform.pub.aop.LogPurview)",returning = "object")
    public Object LogCheck(JoinPoint joinPoint,Object object) throws Throwable {

        HttpServletRequest request=null;
        HttpServletResponse response=null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
            response = requestAttributes.getResponse();
        }
        if (request!=null&&response!=null){
            //获取当前方法
            Signature sign = joinPoint.getSignature();
            MethodSignature signature = null;
            if (!(sign instanceof MethodSignature)){
                throw new IllegalArgumentException("该注解只能作用于方法");
            }
            signature = (MethodSignature) sign;
            Object target = joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(signature.getName(),signature.getParameterTypes());
            LogPurview logPurview = currentMethod.getAnnotation(LogPurview.class);
            if (logPurview.log()){
                System.out.println("请求路径---"+request.getRequestURL()+"------------uri---"+request.getRequestURI());
                System.out.println("方法名---"+joinPoint.getSignature().getName());
                System.out.println("类路径---"+joinPoint.getSignature().getDeclaringTypeName());
                System.out.println("控制器---"+joinPoint.getSignature().getDeclaringType().getSimpleName());
                JSONObject jsonObject = Util.getJSON(request);
                System.out.println("参数---"+jsonObject.toString());
                System.out.println("模块名---"+logPurview.logModule());
                System.out.println("ip地址---"+Util.getIpAddr(request));
                //获取返回的参数，可以处理参数
                DefaultBackMessage backMessage = (DefaultBackMessage) object;
                if (backMessage.isSuccess()){
                    System.out.println("1234567890");
                }
            }
        }
        return object;
    }

    /**
     *  异常返回时走的路径，未处理的异常,
     *  此处是将类注解有LogPurview的才处理异常，如果是将此处的value改为切入点切面pointcut=“”就容易处理所有返回异常统一处理。
     */
    @AfterThrowing(throwing = "ex",value = "@annotation(cn.plateform.pub.aop.LogPurview)")
    public Object LogException(JoinPoint joinPoint,Throwable ex) throws Throwable {
        HttpServletRequest request=null;
        HttpServletResponse response=null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
            response = requestAttributes.getResponse();
        }
        //获取当前方法
        Signature sign = joinPoint.getSignature();
        MethodSignature signature = null;
        if (!(sign instanceof MethodSignature)){
            throw new IllegalArgumentException("该注解只能作用于方法");
        }
        signature = (MethodSignature) sign;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(),signature.getParameterTypes());
        LogPurview logPurview = currentMethod.getAnnotation(LogPurview.class);
        if (logPurview.log()){
            System.out.println("目标方法抛出异常:"+ex.getMessage());
            System.out.println("异常信息："+ex);
            System.out.println("模块："+logPurview.logModule());
            System.out.println("方法名---"+joinPoint.getSignature().getName());
            System.out.println("类路径---"+joinPoint.getSignature().getDeclaringTypeName());
            System.out.println("控制器---"+joinPoint.getSignature().getDeclaringType().getSimpleName());
            System.out.println("请求路径---"+request.getRequestURL()+"------------uri---"+request.getRequestURI());
            System.out.println("ip地址---"+Util.getIpAddr(request));
            JSONObject jsonObject = Util.getJSON(request);
            System.out.println("参数---"+jsonObject);
        }

        //统一异常处理
        if (ex instanceof Exception){
            throw new ServiceException();
        }else  {
            return DefaultBackMessage.fail("未知");
        }
    }

}
