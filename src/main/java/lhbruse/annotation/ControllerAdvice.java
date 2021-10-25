package lhbruse.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器增强器
 * @author: LHB
 * @createDate: 2021/9/13
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ControllerAdvice {

    /**
     * 指定controller生效
     */
    Class<?>[] assignableTypes() default {};
}
