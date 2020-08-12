package cn.plateform.pub.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target 此注解的作用目标，括号里METHOD的意思说明此注解只能加在方法上面,ElementType.TYPE  TYPE意思是可注解于类上
 * @Retention 注解的保留位置，括号里RUNTIME的意思说明注解可以存在于运行时，可以用于反射
 * @Documented 说明该注解将包含在javadoc中
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPurview {

    /**
     * 是否需要记录操作日志
     * @return
     */
    boolean log() default false;

    /**
     * 日志模块
     */
    String logModule() default "系统模块";

}
