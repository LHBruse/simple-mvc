package lhbruse.argument;

import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class ModelMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response,
                                  ConversionService conversionService) throws Exception {
        Assert.state(mavContainer != null, "ModelAndView is required for model exposure");
        return mavContainer.getModel();
    }
}
