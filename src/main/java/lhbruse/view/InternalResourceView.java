package lhbruse.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author: LHB
 * @createDate: 2021/9/7
 * @version: 1.0
 */
@SuppressWarnings("ALL")
public class InternalResourceView extends AbstractView {

    private String url;

    public InternalResourceView(String url) {
        this.url = url;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> mergedModel, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        exposeModelAsRequestAttributes(mergedModel, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(this.url);
        dispatcher.forward(request, response);
    }

    /**
     * 把model中的数据放入到request
     */
    private void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {
        model.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String name, Object value) {
                if (value != null) {
                    request.setAttribute(name, value);
                } else {
                    request.removeAttribute(name);
                }
            }
        });
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
