package lhbruse.handler;

import lhbruse.argument.HandlerMethodArgumentResolver;
import lhbruse.http.HttpStatus;
import lhbruse.returnValue.HandlerMethodReturnValueHandler;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Objects;

/**
 * 记录{@link HandlerMethodArgumentResolver}和
 * {@link HandlerMethodReturnValueHandler}在调用控制器方法过程中做出的与模型和视图相关的决策。
 *
 * @author: LHB
 * @createDate: 2021/9/9
 * @version: 1.0
 */
public class ModelAndViewContainer {

    private Object view;

    private Model model;

    private HttpStatus httpStatus;

    private boolean requestHandled = false;

    public String getViewName() {
        return view instanceof String ? (String) view : null;
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public boolean isViewReference() {
        return (this.view instanceof String);
    }

    public Model getModel() {
        if (Objects.isNull(this.model)) {
            this.model = new ExtendedModelMap();
        }
        return this.model;
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
