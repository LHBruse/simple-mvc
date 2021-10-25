package lhbruse.config;

import lhbruse.argument.HandlerMethodArgumentResolver;
import lhbruse.handler.interceptor.InterceptorRegistry;
import lhbruse.returnValue.HandlerMethodReturnValueHandler;
import lhbruse.view.View;
import lhbruse.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistry;

import java.util.List;

/**
 * 自定义Java基础配置
 *
 * @author: LHB
 * @createDate: 2021/10/20
 * @version: 1.0
 */
public interface WebMvcConfigurer {

    /**
     * 添加定义转换器 或 格式化输出转换器，数据转换格式化
     */
    default void addFormatters(FormatterRegistry registry) {
    }

    /**
     * 添加SpringMVC拦截器，用于控制器调用的预处理和后处理。
     */
    default void addInterceptors(InterceptorRegistry registry) {

    }

    /**
     * 添加要使用的自定义HandlerMethodArgumentResolver
     */
    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    /**
     * 添加要使用的自定义HandlerMethodReturnValueHandler
     */
    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    /**
     * 添加自定义的ViewResolvers
     */
    default void addViewResolvers(List<ViewResolver> resolvers) {

    }

    /**
     * 添加自定义的View
     */
    default void addDefaultViews(List<View> views) {

    }


}
