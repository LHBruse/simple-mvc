package lhbruse.handler.interceptor;

import lhbruse.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器拦截器
 * @author: LHB
 * @createDate: 2021/8/25
 * @version: 1.0
 */
public interface HandlerInterceptor {

    /**
     * 在执行Handler之前被调用，如果返回的是false，那么Handler就不会在执行
     */
    default boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        return true;
    }

    /**
     * 在Handler执行完成之后被调用，可以获取Handler返回的结果ModelAndView
     */
    default void postHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){

    }

    /**
     * 该方法是无论什么情况下都会被调用
     */
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception){

    }

}
