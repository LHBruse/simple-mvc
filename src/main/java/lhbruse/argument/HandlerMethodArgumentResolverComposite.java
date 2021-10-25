package lhbruse.argument;

import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 通过委托给已注册的HandlerMethodArgumentResolver列表来解析方法参数。
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    public void addResolver(HandlerMethodArgumentResolver argumentResolver) {
        this.argumentResolvers.add(argumentResolver);
    }

    public void addResolvers(HandlerMethodArgumentResolver... argumentResolvers) {
        this.argumentResolvers.addAll(Arrays.asList(argumentResolvers));
    }

    public void addResolvers(Collection<HandlerMethodArgumentResolver> resolvers){
        this.argumentResolvers.addAll(resolvers);
    }

    public void clear() {
        this.argumentResolvers.clear();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response,
                                  ConversionService conversionService) throws Exception {
        for (HandlerMethodArgumentResolver argumentResolver : this.argumentResolvers) {
            if (argumentResolver.supportsParameter(parameter)) {
                return argumentResolver.resolveArgument(parameter, mavContainer, request, response, conversionService);
            }
        }
        throw new IllegalArgumentException("Unsupported parameter type [" +
                parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
    }
}
