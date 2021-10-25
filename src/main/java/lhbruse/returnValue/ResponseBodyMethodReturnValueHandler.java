package lhbruse.returnValue;

import com.alibaba.fastjson.JSON;
import lhbruse.ModelAndView;
import lhbruse.annotation.ResponseBody;
import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果handler方法上被注解@ResponseBody标注，那么返回JSON字符串
 *
 * @author: LHB
 * @createDate: 2021/9/1
 * @version: 1.0
 */
public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnParameter) {
        return AnnotatedElementUtils.hasAnnotation(returnParameter.getContainingClass(), ResponseBody.class) ||
                returnParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public void handlerReturnValue(Object returnValue, MethodParameter returnParameter,
                                   ModelAndViewContainer mavContainer, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        //标记出当前请求已经处理完成，后续的渲染无需在执行
        mavContainer.setRequestHandled(true);
        response.getWriter().write(JSON.toJSONString(returnValue));
    }
}
