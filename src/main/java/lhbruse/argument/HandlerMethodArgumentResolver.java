package lhbruse.argument;

import lhbruse.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 策略接口，作用是把请求中的数据解析为Controller中方法的参数值
 *
 * @author: LHB
 * @createDate: 2021/8/26
 * @version: 1.0
 */
public interface HandlerMethodArgumentResolver {

    /**
     * 判断当前的参数解析器是否支持传入的参数，返回true表示支持
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 从request对象中解析出parameter需要的值
     */
    Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                           HttpServletRequest request, HttpServletResponse response,
                           ConversionService conversionService) throws Exception;

}
