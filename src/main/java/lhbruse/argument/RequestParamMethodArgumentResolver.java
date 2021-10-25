package lhbruse.argument;

import lhbruse.annotation.RequestParam;
import lhbruse.exception.MissingServletRequestParameterException;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析Controller方法中带有{@link RequestParam}的参数
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response,
                                  ConversionService conversionService) throws Exception {
        RequestParam parameterAnnotation = parameter.getParameterAnnotation(RequestParam.class);
        if (parameterAnnotation == null) {
            return null;
        }
        String value = request.getParameter(parameterAnnotation.name());
        if (!StringUtils.hasLength(value)) {
            value = parameterAnnotation.value();
        }
        if (StringUtils.hasLength(value)) {
            return conversionService.convert(value, parameter.getParameterType());
        }
        if(parameterAnnotation.required()){
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }
        return null;
    }
}
