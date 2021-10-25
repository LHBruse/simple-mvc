package lhbruse.handler.interceptor;

import lhbruse.ModelAndView;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截器，用于控制器调用的预处理和后处理
 * @author: LHB
 * @createDate: 2021/8/25
 * @version: 1.0
 */
public class MappedInterceptor implements HandlerInterceptor {

    /**
     * 需要拦截的path
     */
    private final List<String> includePatterns = new ArrayList<>();
    /**
     * 直接放行的path
     */
    private final List<String> excludePatterns = new ArrayList<>();

    private final HandlerInterceptor handlerInterceptor;

    public MappedInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    /**
     * 添加拦截的路径
     */
    public MappedInterceptor addIncludePatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 添加直接放行的路径
     */
    public MappedInterceptor addExcludePatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 判断拦截器是否需要拦截该path
     */
    public boolean match(String path) {
        if (!CollectionUtils.isEmpty(this.excludePatterns)) {
            if (this.excludePatterns.contains(path)) {
                return false;
            }
        }
        if (CollectionUtils.isEmpty(this.includePatterns)) {
            return true;
        }
        if (this.includePatterns.contains(path)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.handlerInterceptor.preHandler(request, response, handler);
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        this.handlerInterceptor.postHandler(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        this.handlerInterceptor.afterCompletion(request, response, handler, exception);
    }
}
