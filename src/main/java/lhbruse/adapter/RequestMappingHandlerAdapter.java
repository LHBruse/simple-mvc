package lhbruse.adapter;

import lhbruse.ModelAndView;
import lhbruse.argument.*;
import lhbruse.handler.HandlerMethod;
import lhbruse.handler.InvocableHandlerMethod;
import lhbruse.handler.ModelAndViewContainer;
import lhbruse.returnValue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 将某个请求适配给@RequestMapping注释的Handler处理
 *
 * @author: LHB
 * @createDate: 2021/9/6
 * @version: 1.0
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    /**
     * 用户自定义的参数解析器
     */
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    /**
     * 用户自定义的handler方法返回值处理器
     */
    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService can not null");
        if (this.argumentResolverComposite == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolvers(resolvers);
        }
        if (this.returnValueHandlerComposite == null) {
            List<HandlerMethodReturnValueHandler> returnValueHandlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandlers(returnValueHandlers);
        }


    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        InvocableHandlerMethod invocableHandlerMethod = createInvocableHandlerMethod(handlerMethod);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        invocableHandlerMethod.invokeAndHandle(request, response, mavContainer);
        return getModelAndView(mavContainer);
    }

    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod,
                this.argumentResolverComposite,
                this.returnValueHandlerComposite,
                this.conversionService);
    }

    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer) {
        if (mavContainer.isRequestHandled()) {
            //本次请求已经处理完成
            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(mavContainer.getView());
        modelAndView.setModel(mavContainer.getModel());
        modelAndView.setHttpStatus(mavContainer.getHttpStatus());
        return modelAndView;
    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new ServletRequestMethodArgumentResolver());
        argumentResolvers.add(new ServletResponseMethodArgumentResolver());
        argumentResolvers.add(new RequestBodyMethodArgumentResolver());
        argumentResolvers.add(new RequestParamMethodArgumentResolver());
        argumentResolvers.add(new ModelMethodArgumentResolver());
        if (!CollectionUtils.isEmpty(this.customArgumentResolvers)) {
            argumentResolvers.addAll(this.customArgumentResolvers);
        }
        return argumentResolvers;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
        returnValueHandlers.add(new MapMethodReturnValueHandler());
        returnValueHandlers.add(new ModelMethodReturnValueHandler());
        returnValueHandlers.add(new ResponseBodyMethodReturnValueHandler());
        returnValueHandlers.add(new ViewMethodReturnValueHandler());
        returnValueHandlers.add(new ViewNameMethodReturnValueHandler());
        if (!CollectionUtils.isEmpty(this.customReturnValueHandlers)) {
            returnValueHandlers.addAll(this.customReturnValueHandlers);
        }
        return returnValueHandlers;
    }

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
}
