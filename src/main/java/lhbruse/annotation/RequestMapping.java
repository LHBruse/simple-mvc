package lhbruse.annotation;

import lhbruse.http.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求映射
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * url路径
     */
    String path();

    RequestMethod method() default RequestMethod.GET;
}
