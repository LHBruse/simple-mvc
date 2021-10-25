package lhbruse.config;

import lhbruse.argument.HandlerMethodArgumentResolver;
import lhbruse.handler.interceptor.InterceptorRegistry;
import lhbruse.returnValue.HandlerMethodReturnValueHandler;
import lhbruse.view.View;
import lhbruse.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 将所有自定义的Java配置委托给该类统一配置
 *
 * @author: LHB
 * @createDate: 2021/10/20
 * @version: 1.0
 */
public class WebMvcConfigurerComposite implements WebMvcConfigurer {
    List<WebMvcConfigurer> delegates = new ArrayList<>();

    @Override
    public void addFormatters(FormatterRegistry registry) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addFormatters(registry));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addInterceptors(registry));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addArgumentResolvers(argumentResolvers));
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addReturnValueHandlers(returnValueHandlers));
    }

    @Override
    public void addViewResolvers(List<ViewResolver> resolvers) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addViewResolvers(resolvers));
    }

    @Override
    public void addDefaultViews(List<View> views) {
        delegates.forEach(webMvcConfigurer -> webMvcConfigurer.addDefaultViews(views));
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(WebMvcConfigurer... webMvcConfigurers) {
        Collections.addAll(this.delegates, webMvcConfigurers);
        return this;
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers) {
        this.delegates.addAll(webMvcConfigurers);
        return this;
    }
}
