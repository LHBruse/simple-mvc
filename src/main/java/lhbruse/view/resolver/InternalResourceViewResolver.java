package lhbruse.view.resolver;

import lhbruse.view.InternalResourceView;
import lhbruse.view.View;

/**
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public class InternalResourceViewResolver extends UrlBasedViewResolver{
    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
