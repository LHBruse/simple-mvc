package lhbruse.handler.mapping;

import lhbruse.annotation.RequestMapping;
import lhbruse.exception.NoHandlerFoundException;
import lhbruse.handler.HandlerExecutionChain;
import lhbruse.handler.HandlerMethod;
import lhbruse.handler.interceptor.HandlerInterceptor;
import lhbruse.handler.interceptor.MappedInterceptor;
import lhbruse.http.RequestMethod;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 初始化 请求路径映射控制器方法的注册中心
 *
 * @author: LHB
 * @createDate: 2021/8/24
 * @version: 1.0
 */
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();

    /**
     * 所有的拦截器
     */
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    /**
     * 在初始化bean的时候会执行该方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initialPathHandlerMethodMappings();
    }

    /**
     * 初始化请求路径到控制器方法的映射
     */
    private void initialPathHandlerMethodMappings() {
        Map<String, Object> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);
        beansMap.entrySet().stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethod(entry.getValue()));
    }

    /**
     * Controller注解注释的类就是handler
     */
    private boolean isHandler(Object handler) {
        return AnnotatedElementUtils.hasAnnotation(handler.getClass(), Controller.class);
    }

    /**
     * 获取控制器handler中被{@link lhbruse.annotation.RequestMapping} 注释的方法，
     * 并注册到 {@link MappingRegistry}注册中心中
     */
    private void detectHandlerMethod(Object handler) {
        Map<Method, RequestMappingInfo> selectMethods = MethodIntrospector.selectMethods(handler.getClass(), new MethodIntrospector.MetadataLookup<RequestMappingInfo>() {
            @Override
            public RequestMappingInfo inspect(Method method) {
                return getRequestMappingInfoForMethod(method, handler.getClass());
            }
        });

        selectMethods.forEach((method, requestMappingInfo) -> mappingRegistry.register(requestMappingInfo, handler, method));
    }

    /**
     * 根据被{@link RequestMapping}注释的控制器方法构建{@link RequestMappingInfo}对象
     *
     * @param method 控制器方法
     * @return 请求路径映射信息
     */
    private RequestMappingInfo getRequestMappingInfoForMethod(Method method, Class<?> beanType) {
        //在element上查询annotationType类型注解，将查询出的多个annotationType类型注解属性合并到查询的第一个注解中
        RequestMapping classMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        //获取被RequestMapping注释的类的请求路径
        String pathPrefix = classMapping == null ? "" : classMapping.path();
        //获取被RequestMapping注释的方法上的信息
        RequestMapping methodMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (methodMapping == null) {
            return null;
        }
        return new RequestMappingInfo(pathPrefix, methodMapping);
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(RequestMethod.class, request.getMethod());
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(requestURI, requestMethod);
        HandlerMethod handler = mappingRegistry.getHandlerMethod(requestMappingInfo);
        if (handler == null) {
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(requestURI, handler);
    }

    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> declareInterceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.match(lookupPath))
                .collect(Collectors.toList());
        return new HandlerExecutionChain(handler, declareInterceptors);
    }

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
