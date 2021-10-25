package lhbruse.annotation;

import lhbruse.config.DelegatingWebMvcConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: LHB
 * @createDate: 2021/10/21
 * @version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
