package lhbruse.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web请求参数绑定到方法参数的注解
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * 从request取参数的名字
     */
    @AliasFor("value")
    String name() default "";

    /**
     * name的别名
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 该参数是否必填
     */
    boolean required() default true;
}
