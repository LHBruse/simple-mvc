package lhbruse.handler.mapping;

import lhbruse.handler.HandlerMethod;
import lhbruse.http.RequestMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求路径映射控制器方法的注册中心
 * @author: LHB
 * @createDate: 2021/8/24
 * @version: 1.0
 */
public class MappingRegistry {

    /**
     * 请求到控制器方法的映射
     */
    private Map<RequestMappingInfo, HandlerMethod> pathHandlerMethodMap = new ConcurrentHashMap<>();

    /**
     * 注册RequestMappingInfo和HandlerMethod的映射关系
     *
     * @param requestMappingInfo 请求路径信息
     * @param handler            控制器
     * @param method             {@link lhbruse.annotation.RequestMapping}注释的方法
     */
    public void register(RequestMappingInfo requestMappingInfo, Object handler, Method method) {
        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHandlerMethodMap.put(requestMappingInfo, handlerMethod);
    }

    public HandlerMethod getHandlerMethod(RequestMappingInfo requestMappingInfo) {
        return this.pathHandlerMethodMap.get(requestMappingInfo);
    }

}
