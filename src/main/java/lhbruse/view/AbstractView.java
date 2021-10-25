package lhbruse.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author: LHB
 * @createDate: 2021/9/6
 * @version: 1.0
 */
public abstract class AbstractView implements View {

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.prepareResponse(request, response);
        this.renderMergedOutputModel(model, request, response);
    }

    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response){

    }

    /**
     * 渲染视图并输出
     */
    protected void renderMergedOutputModel(Map<String, Object> mergedModel, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }
}
