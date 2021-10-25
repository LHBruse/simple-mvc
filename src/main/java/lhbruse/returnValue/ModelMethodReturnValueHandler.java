package lhbruse.returnValue;

import lhbruse.ModelAndView;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支持Handler返回Model值，放入到上下文中，用于页面渲染使用
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return Model.class.isAssignableFrom(returnParameter.getParameterType());
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        if (returnValue == null) {
            return;
        } else if (returnValue instanceof Model) {
            mavContainer.getModel().addAllAttributes(((Model) returnValue).asMap());
        } else {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnParameter.getParameterType().getName() + " in method: " + returnParameter.getMethod());
        }

    }
}
