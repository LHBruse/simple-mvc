package lhbruse.handler.exception;

import lhbruse.ModelAndView;
import lhbruse.argument.*;
import lhbruse.handler.InvocableHandlerMethod;
import lhbruse.handler.ModelAndViewContainer;
import lhbruse.returnValue.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 通过@ExceptionHandler标注的方法处理异常
 *
 * @author: LHB
 * @createDate: 2021/10/8
 * @version: 1.0
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver, ApplicationContextAware, InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<>();

    @Nullable
    private ApplicationContext applicationContext;
    
    public static final ModelAndView COMPLETE_MODEL_AND_VIEW = new ModelAndView();

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache() {
        return Collections.unmodifiableMap(this.exceptionHandlerAdviceCache);
    }

    @Override
    public void afterPropertiesSet() {
        initExceptionHandlerAdviceCache();
        if (this.argumentResolverComposite == null) {
            List<HandlerMethodArgumentResolver> defaultArgumentResolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolvers(defaultArgumentResolvers);
        }
        if (this.returnValueHandlerComposite == null) {
            List<HandlerMethodReturnValueHandler> defaultReturnValueHandlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandlers(defaultReturnValueHandlers);
        }
    }

    private void initExceptionHandlerAdviceCache() {
        if (getApplicationContext() == null) {
            return;
        }
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMapping()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
            if (logger.isDebugEnabled()) {
                int size = this.exceptionHandlerAdviceCache.size();
                if (size == 0) {
                    logger.debug("ControllerAdvice beans: none");
                } else {
                    logger.debug("ControllerAdvice beans: " + size);
                }
            }
        }
    }

    protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        if (!CollectionUtils.isEmpty(getCustomArgumentResolvers())) {
            resolvers.addAll(getCustomArgumentResolvers());
        }
        return resolvers;
    }

    protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
        returnValueHandlers.add(new ViewNameMethodReturnValueHandler());
        returnValueHandlers.add(new ViewMethodReturnValueHandler());
        returnValueHandlers.add(new ResponseBodyMethodReturnValueHandler());
        returnValueHandlers.add(new ModelMethodReturnValueHandler());
        returnValueHandlers.add(new MapMethodReturnValueHandler());
        if (!CollectionUtils.isEmpty(getCustomReturnValueHandlers())) {
            returnValueHandlers.addAll(getDefaultReturnValueHandlers());
        }
        return returnValueHandlers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Nullable
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 找到@ExceptionHandler方法并调用它来处理引发的异常
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        InvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Using @ExceptionHandler " + exceptionHandlerMethod);
            }
            Throwable cause = ex.getCause();
            if (cause != null) {
                exceptionHandlerMethod.invokeAndHandle(request, response, modelAndViewContainer, cause);
            } else {
                exceptionHandlerMethod.invokeAndHandle(request, response, modelAndViewContainer, ex);
            }
        } catch (Exception exception) {
            logger.error("exceptionHandlerMethod.invokeAndHandle fail", exception);
            return null;
        }
        if (modelAndViewContainer.isRequestHandled()) {
            return COMPLETE_MODEL_AND_VIEW;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setModel(modelAndViewContainer.getModel());
        modelAndView.setView(modelAndViewContainer.getView());
        modelAndView.setHttpStatus(modelAndViewContainer.getHttpStatus());
        return modelAndView;
    }

    /**
     * 查找给定异常的@ExceptionHandler方法。
     */
    private InvocableHandlerMethod getExceptionHandlerMethod(Exception exception) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean adviceBean = entry.getKey();
            ExceptionHandlerMethodResolver methodResolver = entry.getValue();
            Method method = methodResolver.resolveMethod(exception);
            if (method != null) {
                return new InvocableHandlerMethod(adviceBean.getBean(),
                        method,
                        this.argumentResolverComposite,
                        this.returnValueHandlerComposite,
                        this.conversionService);
            }
        }
        return null;
    }

}
