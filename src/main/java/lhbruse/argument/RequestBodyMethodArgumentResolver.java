package lhbruse.argument;

import com.alibaba.fastjson.JSON;
import lhbruse.annotation.RequestBody;
import lhbruse.exception.MissingServletRequestParameterException;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response,
                                  ConversionService conversionService) throws Exception {
        String httpMessageBody = this.getHttpMessageBody(request);
        if (StringUtils.hasText(httpMessageBody)) {
            return JSON.parseObject(httpMessageBody, parameter.getParameterType());
        }
        RequestBody parameterAnnotation = parameter.getParameterAnnotation(RequestBody.class);
        if (parameterAnnotation == null) {
            return null;
        }
        if (parameterAnnotation.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }
        return null;
    }

    private String getHttpMessageBody(HttpServletRequest request) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        char[] chars = new char[1024];
        int len;
        while ((len = reader.read(chars)) != -1) {
            buffer.append(chars, 0, len);
        }
        return buffer.toString();
    }
}
