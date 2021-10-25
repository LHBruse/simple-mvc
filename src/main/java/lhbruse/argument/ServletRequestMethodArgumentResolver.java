package lhbruse.argument;

import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析Controller中方法的request参数
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return ServletRequest.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response,
                                  ConversionService conversionService) throws Exception {
        return request;
    }
}
