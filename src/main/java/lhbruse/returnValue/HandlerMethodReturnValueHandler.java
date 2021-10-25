package lhbruse.returnValue;

import lhbruse.ModelAndView;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理从handler方法调用返回的值
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public interface HandlerMethodReturnValueHandler {

    /**
     * 判断处理器是否支持该返回值的类型
     */
    boolean supportsReturnType(MethodParameter returnParameter);

    /**
     * 通过向model添加属性并设置视图或将ModelAndViewContainer.setRequestHandled标志
     * 设置为true来处理给定的返回值，以指示已直接处理响应。
     */
    void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                            ModelAndViewContainer mavContainer, HttpServletRequest request,
                            HttpServletResponse response) throws Exception;
}
