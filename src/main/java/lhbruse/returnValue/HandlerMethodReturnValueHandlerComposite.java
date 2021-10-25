package lhbruse.returnValue;

import lhbruse.ModelAndView;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 通过委托给已注册的HandlerMethodReturnValueHandlers列表来处理方法返回值。
 * @author: LHB
 * @createDate: 2021/9/1
 * @version: 1.0
 */
public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {
    private List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return getReturnValueHandler(returnParameter) != null;
    }

    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnParameter) {
        for (HandlerMethodReturnValueHandler returnValueHandler : this.returnValueHandlers) {
            if (returnValueHandler.supportsReturnType(returnParameter)) {
                return returnValueHandler;
            }
        }
        return null;
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnParameter)) {
                handler.handlerReturnValue(returnValue, returnParameter, mavContainer, request, response);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported parameter type [" +
                returnParameter.getParameterType().getName() + "]. supportsParameter should be called first.");
    }

    public void clear() {
        this.returnValueHandlers.clear();
    }

    public void addReturnValueHandlers(HandlerMethodReturnValueHandler... handlers) {
        Collections.addAll(this.returnValueHandlers, handlers);
    }

    public void addReturnValueHandlers(Collection<HandlerMethodReturnValueHandler> handlers) {
        this.returnValueHandlers.addAll(handlers);
    }
}
