package lhbruse.handler;

import lhbruse.ModelAndView;
import lhbruse.handler.interceptor.HandlerInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
public class HandlerExecutionChain {

    private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);

    private final HandlerMethod handler;

    /**
     * 匹配请求路径path的拦截器
     */
    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    private int interceptorIndex = -1;


    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if (!CollectionUtils.isEmpty(interceptors)) {
            this.interceptors = interceptors;
        }
    }

    /**
     * 执行所有注册了的拦截器的 PreHandler方法
     */
    public boolean applyPreHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CollectionUtils.isEmpty(this.interceptors)) {
            return true;
        }
        //遍历所有拦截器
        for (int i = 0; i < this.interceptors.size(); i++) {
            HandlerInterceptor handlerInterceptor = this.interceptors.get(i);
            if (!handlerInterceptor.preHandler(request, response, handler)) {
                this.triggerAfterCompletion(request, response, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;

    }

    /**
     * 执行所有拦截器的postHandle方法
     */
    public void applyPostHandler(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        if (!CollectionUtils.isEmpty(this.interceptors)) {
            for (int i = this.interceptors.size() - 1; i >= 0; i--) {
                HandlerInterceptor handlerInterceptor = this.interceptors.get(i);
                handlerInterceptor.postHandler(request, response, this.handler, modelAndView);
            }
        }

    }

    /**
     * 根据interceptorIndex记录的下标值反向执行拦截器的afterCompletion方法
     */
    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if (!CollectionUtils.isEmpty(this.interceptors)) {
            for (int i = this.interceptorIndex; i > 0; i--) {
                HandlerInterceptor handlerInterceptor = this.interceptors.get(i);
                try {
                    handlerInterceptor.afterCompletion(request, response, handler, exception);
                } catch (Throwable throwable) {
                    logger.error("HandlerInterceptor.afterCompletion threw exception", throwable);
                }

            }
        }
    }

    public List<HandlerInterceptor> getInterceptors() {
        return this.interceptors;
    }

    public HandlerMethod getHandler() {
        return this.handler;
    }
}
