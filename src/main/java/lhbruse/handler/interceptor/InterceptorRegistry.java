package lhbruse.handler.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器注册器
 * @author: LHB
 * @createDate: 2021/8/25
 * @version: 1.0
 */
public class InterceptorRegistry {
    /**
     * 请求路径映射拦截器集合
     */
    private final List<MappedInterceptor> interceptors = new ArrayList<>();

    public MappedInterceptor registry(HandlerInterceptor handlerInterceptor) {
        MappedInterceptor mappedInterceptor = new MappedInterceptor(handlerInterceptor);
        this.interceptors.add(mappedInterceptor);
        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptors() {
        return this.interceptors;
    }
}
