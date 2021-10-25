package lhbruse.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: LHB
 * @createDate: 2021/9/23
 * @version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {

    /**
     * 可以指定多个异常类，表示被标注的方法能处理指定的异常
     */
    Class<? extends Throwable>[] value() default {};
}
