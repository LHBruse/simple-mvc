package lhbruse.config;

import lhbruse.adapter.HandlerAdapter;
import lhbruse.adapter.RequestMappingHandlerAdapter;
import lhbruse.argument.HandlerMethodArgumentResolver;
import lhbruse.handler.exception.ExceptionHandlerExceptionResolver;
import lhbruse.handler.exception.HandlerExceptionResolver;
import lhbruse.handler.interceptor.InterceptorRegistry;
import lhbruse.handler.interceptor.MappedInterceptor;
import lhbruse.handler.mapping.HandlerMapping;
import lhbruse.handler.mapping.RequestMappingHandlerMapping;
import lhbruse.returnValue.HandlerMethodReturnValueHandler;
import lhbruse.view.View;
import lhbruse.view.resolver.ContentNegotiatingViewResolver;
import lhbruse.view.resolver.InternalResourceViewResolver;
import lhbruse.view.resolver.ViewResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 提供MVC Java配置的主类
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
public class WebMvcConfigurationSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private List<MappedInterceptor> mappedInterceptors;

    private List<HandlerMethodArgumentResolver> argumentResolvers;

    private List<HandlerMethodReturnValueHandler> returnValueHandlers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 构建数据转换器，（将http请求的string转换成java bean或输出时转换成json）
     */
    @Bean
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;
    }

    /**
     * 重写此方法以将 自定义转换器 或 格式化输出 添加到公共FormattingConversionService。
     */
    protected void addFormatters(FormatterRegistry formatterRegistry) {

    }

    /**
     * 构建HandlerMapper
     */
    @Bean
    public HandlerMapping handlerMapping(FormattingConversionService conversionService) {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setInterceptors(getInterceptor(conversionService));
        return handlerMapping;
    }

    /**
     * 构建请求拦截器，默认没有拦截器
     */
    protected final List<MappedInterceptor> getInterceptor(FormattingConversionService mvcConversionService) {
        if (this.mappedInterceptors == null) {
            InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
            addInterceptors(interceptorRegistry);
            this.mappedInterceptors = interceptorRegistry.getMappedInterceptors();
        }
        return this.mappedInterceptors;
    }

    /**
     * 重写此方法以添加SpringMVC拦截器，用于控制器调用的预处理和后处理。
     */
    protected void addInterceptors(InterceptorRegistry registry) {

    }
    
    /**
     * 构建HandlerAdapter
     */
    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setCustomArgumentResolvers(getArgumentResolvers());
        handlerAdapter.setCustomReturnValueHandlers(getReturnValueHandlers());
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }

    /**
     * 构建HandlerExceptionResolver
     */
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(FormattingConversionService conversionService) {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setCustomArgumentResolvers(getArgumentResolvers());
        exceptionResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
        exceptionResolver.setConversionService(conversionService);
        return exceptionResolver;
    }

    /**
     * 提供对RequestMappingHandlerAdapter和ExceptionHandlerExceptionResolver使用的共享自定义参数解析器的访问。
     */
    protected final List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if (this.argumentResolvers == null) {
            this.argumentResolvers = new ArrayList<>();
            addArgumentResolvers(this.argumentResolvers);
        }
        return this.argumentResolvers;
    }

    /**
     * 添加要使用的自定义HandlerMethodArgumentResolver
     */
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    /**
     * 提供对RequestMappingHandlerAdapter和ExceptionHandlerExceptionResolver使用的共享返回值处理器的访问。
     */
    protected final List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        if (this.returnValueHandlers == null) {
            this.returnValueHandlers = new ArrayList<>();
            addReturnValueHandlers(this.returnValueHandlers);
        }
        return this.returnValueHandlers;
    }

    /**
     * 添加要使用的自定义HandlerMethodReturnValueHandler
     */
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    /**
     * 注册包含用于视图解析的ViewResolver组件。
     */
    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        List<ViewResolver> viewResolvers = new ArrayList<>();
        addViewResolvers(viewResolvers);
        if (CollectionUtils.isEmpty(viewResolvers)) {
            negotiatingViewResolver.setResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        } else {
            negotiatingViewResolver.setResolvers(viewResolvers);
        }
        List<View> views = new ArrayList<>();
        addDefaultViews(views);
        if (!CollectionUtils.isEmpty(views)) {
            negotiatingViewResolver.setDefaultViews(views);
        }
        return negotiatingViewResolver;
    }

    /**
     * 添加自定义的ViewResolvers
     */
    protected void addViewResolvers(List<ViewResolver> resolvers) {

    }

    /**
     * 添加自定义的View
     */
    protected void addDefaultViews(List<View> views) {

    }

}
