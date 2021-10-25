package lhbruse.handler;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装控制器中的方法
 *
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
public class HandlerMethod {
    /**
     * 控制器实体类对象
     */
    private Object bean;

    /**
     * 控制器实体类类型
     */
    private Class<?> beanType;

    /**
     * {@link lhbruse.annotation.RequestMapping} 注释的方法
     */
    private Method method;

    /**
     * 方法中的所有参数信息
     */
    private List<MethodParameter> methodParameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;
        this.methodParameters = new ArrayList<>();
        int parameterCount = method.getParameterCount();
        for (int i = 0; i < parameterCount; i++) {
            this.methodParameters.add(new MethodParameter(method, i));
        }
    }

    public HandlerMethod(HandlerMethod handlerMethod) {
        Assert.notNull(handlerMethod, "HandlerMethod is required");
        this.bean = handlerMethod.bean;
        this.beanType = handlerMethod.beanType;
        this.method = handlerMethod.method;
        this.methodParameters = handlerMethod.methodParameters;
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getMethodParameters() {
        return methodParameters;
    }
}
