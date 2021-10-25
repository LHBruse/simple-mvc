package lhbruse.returnValue;

import lhbruse.ModelAndView;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支持返回一个String对象，表示视图的路径
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return CharSequence.class.isAssignableFrom(returnParameter.getParameterType());
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        if (returnValue instanceof CharSequence) {
            mavContainer.setViewName(returnValue.toString());
        } else if (returnValue != null) {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnParameter.getParameterType().getName() + " in method: " + returnParameter.getMethod());
        }
    }
}
