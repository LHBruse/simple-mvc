package lhbruse.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用于web交互的MVC视图，完成内容的渲染
 *
 * @author: LHB
 * @createDate: 2021/8/30
 * @version: 1.0
 */
public interface View {

    /**
     * 返回控制视图支持的ContentType
     */
    default String getContentType() {
        return null;
    }

    /**
     * 通过response把model中的数据渲染成视图
     */
    void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
