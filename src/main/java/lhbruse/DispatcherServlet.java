package lhbruse;

import lhbruse.adapter.HandlerAdapter;
import lhbruse.handler.exception.HandlerExceptionResolver;
import lhbruse.handler.HandlerExecutionChain;
import lhbruse.handler.mapping.HandlerMapping;
import lhbruse.view.View;
import lhbruse.view.resolver.ViewResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {

    private final Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;

    private HandlerAdapter handlerAdapter;

    private ViewResolver viewResolver;

    private Collection<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void init() throws ServletException {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
        this.handlerExceptionResolvers = this.applicationContext.getBeansOfType(HandlerExceptionResolver.class).values();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("DispatcherServlet.service => uri:" + req.getRequestURI());
        RequestContextHolder.setRequest(req);
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RequestContextHolder.resetRequest();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecutionChain handlerExecutionChain = null;
        Exception dispatchException = null;
        try {
            ModelAndView modelAndView = null;
            try {
                handlerExecutionChain = this.handlerMapping.getHandler(req);
                if (!handlerExecutionChain.applyPreHandler(req, resp)) {
                    return;
                }
                modelAndView = this.handlerAdapter.handle(req, resp, handlerExecutionChain.getHandler());
                handlerExecutionChain.applyPostHandler(req, resp, modelAndView);
            } catch (Exception e) {
                dispatchException = e;
            }
            processDispatchResult(req, resp, modelAndView, dispatchException);
        } catch (Exception exception) {
            dispatchException = exception;
            throw exception;
        } finally {
            handlerExecutionChain.triggerAfterCompletion(req, resp, dispatchException);
        }
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView, Exception dispatchException) throws Exception {
        boolean complete = false;
        if (dispatchException != null) {
            modelAndView = processHandlerException(req, resp, dispatchException);
        }
        if (dispatchException == null) {
            render(req, resp, modelAndView);
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        View view;
        String viewName = modelAndView.getViewName();
        if (StringUtils.hasText(viewName)) {
            view = this.viewResolver.resolverViewName(viewName);
        } else {
            view = (View) modelAndView.getView();
        }
        if (modelAndView.getHttpStatus() != null) {
            resp.setStatus(modelAndView.getHttpStatus().getValue());
        }
        view.render(modelAndView.getModel().asMap(), req, resp);
    }

    /**
     * 通过注册的HandlerExceptionResolver确定错误模型和视图。
     */
    private ModelAndView processHandlerException(HttpServletRequest req, HttpServletResponse resp, Exception dispatchException) throws Exception {
        if (this.handlerExceptionResolvers != null) {
            for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
                ModelAndView modelAndView = resolver.resolveException(req, resp, dispatchException);
                if (modelAndView != null) {
                    return modelAndView;
                }
            }
        }
        //未找到对应的异常处理器，就继续抛出异常
        throw dispatchException;
    }


}
