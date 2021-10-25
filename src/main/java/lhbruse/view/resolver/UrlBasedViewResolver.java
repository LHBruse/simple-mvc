package lhbruse.view.resolver;

import lhbruse.view.InternalResourceView;
import lhbruse.view.RedirectView;
import lhbruse.view.View;

/**
 * 将视图名称直接解析为URL
 *
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public abstract class UrlBasedViewResolver extends AbstractCachingViewResolver {

    public static final String REDIRECT_URL_PREFIX = "redirect:";

    public static final String FORWARD_URL_PREFIX = "forward:";

    private String prefix = "";

    private String suffix = "";

    @Override
    protected View createView(String viewName) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl);
        }
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }
        return buildView(viewName);
    }

    protected abstract View buildView(String viewName);

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
