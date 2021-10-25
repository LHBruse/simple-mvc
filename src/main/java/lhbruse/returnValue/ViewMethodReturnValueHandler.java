package lhbruse.returnValue;

import lhbruse.ModelAndView;
import lhbruse.handler.ModelAndViewContainer;
import lhbruse.view.View;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支持Handler直接返回需要渲染的View对象
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return View.class.isAssignableFrom(returnParameter.getParameterType());
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        if (returnValue instanceof View) {
            mavContainer.setView(returnValue);
        } else if (returnValue != null) {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnParameter.getParameterType().getName() + " in method: " + returnParameter.getMethod());
        }
    }
}
