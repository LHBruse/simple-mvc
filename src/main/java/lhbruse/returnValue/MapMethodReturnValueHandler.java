package lhbruse.returnValue;

import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 支持Handler返回Map值，放入到上下文中，用于页面渲染使用
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return Map.class.isAssignableFrom(returnParameter.getParameterType());
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        if (returnValue instanceof Map) {
            mavContainer.getModel().addAllAttributes((Map) returnValue);
        } else if (returnValue != null) {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnParameter.getParameterType().getName() + " in method: " + returnParameter.getMethod());
        }
    }
}
