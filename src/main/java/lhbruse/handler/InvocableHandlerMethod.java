package lhbruse.handler;

import lhbruse.argument.HandlerMethodArgumentResolverComposite;
import lhbruse.returnValue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * HandlerMethod的扩展，通过HandlerMethodArgumentResolver列表从当前请求中解析出控制器的参数值，然后调用控制器的方法。
 *
 * @author: LHB
 * @createDate: 2021/9/2
 * @version: 1.0
 */
public class InvocableHandlerMethod extends HandlerMethod {

    private HandlerMethodArgumentResolverComposite argumentResolver;

    private HandlerMethodReturnValueHandlerComposite returnValueHandler;

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private ConversionService conversionService;

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public void invokeAndHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndViewContainer mavContainer,
                                Object... providedArgs) throws Exception {
        List<Object> args = getMethodArgumentValues(request, response, mavContainer, providedArgs);
        Object returnValue = doInvoke(args);
        if (returnValue == null) {
            if (response.isCommitted()) {
                mavContainer.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }
        mavContainer.setRequestHandled(false);
        Assert.state(this.returnValueHandler != null, "No return value handler");
        //-1表示方法的返回值
        MethodParameter returnParameter = new MethodParameter(this.getMethod(), -1);
        this.returnValueHandler.handlerReturnValue(returnValue, returnParameter, mavContainer, request, response);
    }

    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer mavContainer,
                                                 Object... providedArgs) throws Exception {
        Assert.notNull(this.argumentResolver, "HandlerMethodArgumentResolver can not null");
        List<MethodParameter> methodParameters = this.getMethodParameters();
        ArrayList<Object> args = new ArrayList<>(methodParameters.size());
        for (MethodParameter parameter : methodParameters) {
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            Object arg = findProvidedArgument(parameter, providedArgs);
            if (Objects.nonNull(arg)) {
                args.add(arg);
                continue;
            }
            args.add(this.argumentResolver.resolveArgument(parameter, mavContainer, request, response, this.conversionService));
        }
        return args;
    }

    private Object doInvoke(List<Object> args) throws Exception {
        try {
            return this.getMethod().invoke(this.getBean(), args.toArray());
        } catch (IllegalArgumentException ex) {
            assertTargetBean(this.getMethod(), getBean(), args.toArray());
            String text = (ex.getMessage() != null ? ex.getMessage() : "Illegal argument");
            throw new IllegalStateException(formatInvokeError(text, args.toArray()), ex);
        } catch (InvocationTargetException ex) {
            // Unwrap for HandlerExceptionResolvers ...
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            } else if (targetException instanceof Error) {
                throw (Error) targetException;
            } else if (targetException instanceof Exception) {
                throw (Exception) targetException;
            } else {
                throw new IllegalStateException(formatInvokeError("Invocation failure", args.toArray()), targetException);
            }
        }
    }

    protected static Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                //providedArg 是否能强转为 parameter 类型
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    /**
     * 判断目标bean类是声明给定方法的类的实例。
     */
    protected void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String text = "The mapped handler method class '" + methodDeclaringClass.getName() +
                    "' is not an instance of the actual controller bean class '" +
                    targetBeanClass.getName() + "'. If the controller requires proxying " +
                    "(e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(formatInvokeError(text, args));
        }
    }

    protected String formatInvokeError(String text, Object[] args) {
        String formattedArgs = IntStream.range(0, args.length)
                .mapToObj(i -> (args[i] != null ?
                        "[" + i + "] [type=" + args[i].getClass().getName() + "] [value=" + args[i] + "]" :
                        "[" + i + "] [null]"))
                .collect(Collectors.joining(",\n", " ", " "));
        return text + "\n" +
                "Controller [" + getBeanType().getName() + "]\n" +
                "Method [" + this.getMethod().toGenericString() + "] " +
                "with argument values:\n" + formattedArgs;
    }

}
