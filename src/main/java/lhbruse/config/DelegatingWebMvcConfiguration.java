package lhbruse.config;

import lhbruse.argument.HandlerMethodArgumentResolver;
import lhbruse.handler.interceptor.InterceptorRegistry;
import lhbruse.returnValue.HandlerMethodReturnValueHandler;
import lhbruse.view.View;
import lhbruse.view.resolver.ViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * WebMvcConfigurationSupport的一个子类，用于检测WebMvcConfigurer类型的所有bean并将其委托给它们，
 * 从而允许它们自定义WebMvcConfigurationSupport提供的配置。
 *
 * @author: LHB
 * @createDate: 2021/10/20
 * @version: 1.0
 */
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
    private WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    @Autowired(required = false)
    public void setWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers) {
        if (!CollectionUtils.isEmpty(webMvcConfigurers)) {
            this.configurers.addWebMvcConfigurers(webMvcConfigurers);
        }
    }

    @Override
    protected void addFormatters(FormatterRegistry formatterRegistry) {
        configurers.addFormatters(formatterRegistry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        configurers.addInterceptors(registry);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        configurers.addArgumentResolvers(argumentResolvers);
    }

    @Override
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        configurers.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    protected void addViewResolvers(List<ViewResolver> resolvers) {
        configurers.addViewResolvers(resolvers);
    }

    @Override
    protected void addDefaultViews(List<View> views) {
        configurers.addDefaultViews(views);
    }
}
