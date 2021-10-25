package lhbruse.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 重定向视图，处理handler方法返回的以redirect:开头的视图名称
 *
 * @author: LHB
 * @createDate: 2021/9/7
 * @version: 1.0
 */
public class RedirectView extends AbstractView {

    private String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> mergedModel, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String targetUrl = createTargetUrl(mergedModel, request);
        response.sendRedirect(targetUrl);
    }

    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(this.url, "url can not null");
        StringBuilder queryParams = new StringBuilder();
        model.forEach((key, value) -> queryParams.append(key).append("=").append(value).append("&"));
        if (queryParams.length() > 0) {
            queryParams.deleteCharAt(queryParams.length() - 1);
        }
        StringBuilder targetUrl = new StringBuilder();
        if (this.url.startsWith("/")) {
            targetUrl.append(getContextPath(request));
        }
        targetUrl.append(this.url);
        if (queryParams.length() > 0) {
            targetUrl.append("?").append(queryParams.toString());
        }
        return targetUrl.toString();
    }

    private String getContextPath(HttpServletRequest request) {
        //返回项目的根目录
        String contextPath = request.getContextPath();
        while (contextPath.startsWith("//")) {
            contextPath = contextPath.substring(1);
        }
        return contextPath;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
