package lhbruse.handler.exception;

import lhbruse.annotation.ExceptionHandler;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 找出所有@ExceptionHandler标注的类，包括其所有父类，
 * 解析类中被@ExceptionHandler标注的方法
 *
 * @author: LHB
 * @createDate: 2021/9/29
 * @version: 1.0
 */
public class ExceptionHandlerMethodResolver {

    /**
     * 用于选择@ExceptionHandler方法的筛选器
     */
    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = method ->
            AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);

    private final Map<Class<? extends Throwable>, Method> mappingMethods = new HashMap<>(16);

    private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentReferenceHashMap<>(16);

    /**
     * 在给定类型中查找ExceptionHandler方法的构造函数
     */
    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                addExceptionMapping(exceptionType, method);
            }
        }
    }

    /**
     * 从@ExceptionHandler注释中获取配置的异常，如果@ExceptionHandler注释未配置异常，则从参数中获取
     */
    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
        List<Class<? extends Throwable>> result = new ArrayList<>();
        detectAnnotationExceptionMappings(method, result);
        if (result.isEmpty()) {
            for (Class<?> parameterType : method.getParameterTypes()) {
                if (Throwable.class.isAssignableFrom(parameterType)) {
                    result.add((Class<? extends Throwable>) parameterType);
                }
            }
        }
        if (result.isEmpty()) {
            throw new IllegalStateException("No exception types mapped to " + method);
        }
        return result;
    }

    /**
     * 从@ExceptionHandler注释中获取配置的异常
     */
    private void detectAnnotationExceptionMappings(Method method, List<Class<? extends Throwable>> result) {
        ExceptionHandler annotation = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(annotation != null, "No ExceptionHandler annotation");
        result.addAll(Arrays.asList(annotation.value()));
    }

    /**
     * 添加异常与方法的映射
     */
    private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {
        Method oldMethod = this.mappingMethods.put(exceptionType, method);
        if (oldMethod != null && !oldMethod.equals(method)) {
            throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" +
                    exceptionType + "]: {" + oldMethod + ", " + method + "}");
        }
    }

    /**
     * 包含的类型是否有任何异常映射。
     */
    public boolean hasExceptionMapping() {
        return !this.mappingMethods.isEmpty();
    }

    /**
     * 找到处理给定异常的方法。如果找到多个匹配项，请使用ExceptionDepthComparator。
     */
    public Method resolveMethod(Exception exception) {
        return resolveMethodByThrowable(exception);
    }

    private Method resolveMethodByThrowable(Throwable exception) {
        Method method = resolveMethodByExceptionType(exception.getClass());
        if (method == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                method = resolveMethodByExceptionType(cause.getClass());
            }
        }
        return method;
    }

    public Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionType) {
        Method method = this.exceptionLookupCache.get(exceptionType);
        if (method == null) {
            method = getMappingMethod(exceptionType);
            this.exceptionLookupCache.put(exceptionType, method);
        }
        return method;
    }

    private Method getMappingMethod(Class<? extends Throwable> exceptionType) {
        List<Class<? extends Throwable>> matches = new ArrayList<>();
        for (Class<? extends Throwable> mappedException : this.mappingMethods.keySet()) {
            if (mappedException.isAssignableFrom(exceptionType)) {
                matches.add(mappedException);
            }
        }
        if (!matches.isEmpty()) {
            //按照深度匹配，根据异常的最近继承关系找到继承深度最浅的那个异常
            matches.sort(new ExceptionDepthComparator(exceptionType));
            return this.mappingMethods.get(matches.get(0));
        } else {
            return null;
        }
    }


}
