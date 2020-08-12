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
public @interface AccessPurview {

    /**
     * 是否需要登陆
     * @return
     */
    boolean login() default  false;

    /**
     * 登陆权限
     * @return
     */
    int authType() default 1;

    /**
     * 角色类型
     * @return
     */
    String roleType() default "";

    /**
     * 幂等处理
     * 检测是否重复提交
     * @return
     */
    boolean repeatCheck() default false;

    /**
     * 检测重复有效期长度
     * @return
     */
    int repeatCheckTime() default 3;

    /**
     * 降级处理级别  接口停止访问
     * @return
     */
    int demotionLevel() default 0;
    
}
