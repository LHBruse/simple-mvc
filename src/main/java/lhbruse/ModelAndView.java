package lhbruse;

import lhbruse.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

/**
 * 该类会记录参数解析器HandlerMethodArgumentResolver和Handler返回值解析器HandlerMethodReturnValueHandler
 * 处理过程中Model以及返回的View对象
 *
 * @author: LHB
 * @createDate: 2021/8/26
 * @version: 1.0
 */
public class ModelAndView {

    /**
     * 视图或者试图名称
     */
    private Object view;

    private Model model;

    private HttpStatus httpStatus;

    /**
     * 标记本次请求是否已经处理完成
     */
    private boolean requestHandled = false;

    public Object getView() {
        return view;
    }


    public String getViewName() {
        return this.view instanceof String ? (String) this.view : null;
    }

    public void setViewName(String viewName){
        this.view = viewName;
    }

    /**
     * 视图是否是通过名称指定的视图引用，由 DispatcherServlet 通过 ViewResolver 解析。
     */
    public boolean isViewReference() {
        return (this.view instanceof String);
    }

    public void setView(Object view) {
        this.view = view;
    }

    public Model getModel() {
        if(this.model == null){
            this.model = new ExtendedModelMap();
        }
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }
}
