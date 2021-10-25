package lhbruse.view.resolver;

import lhbruse.RequestContextHolder;
import lhbruse.view.RedirectView;
import lhbruse.view.View;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * 根据用户请求中的头信息Accept匹配出最优的视图
 * ContentNegotingViewResolver本身不解析视图，而是委托给其他视图解析器
 *
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());

    private List<ViewResolver> viewResolvers = new ArrayList<>();
    private List<View> defaultViews = new ArrayList<>();

    @Override
    public View resolverViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);
        View bestView = getBestView(candidateViews);
        if (bestView != null) {
            return bestView;
        }
        return null;
    }

    /**
     * 获取所有候选视图
     */
    private List<View> getCandidateViews(String viewName) throws Exception {
        ArrayList<View> candidateViews = new ArrayList<>();
        for (ViewResolver resolver : this.viewResolvers) {
            View view = resolver.resolverViewName(viewName);
            if (view != null) {
                candidateViews.add(view);
            }
        }
        if (!CollectionUtils.isEmpty(defaultViews)) {
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    private View getBestView(List<View> candidateViews) {
        Optional<View> viewOptional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView).findAny();
        if (viewOptional.isPresent()) {
            return viewOptional.get();
        }
        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> accept = request.getHeaders("accept");
        while (accept.hasMoreElements()) {
            for (View candidateView : candidateViews) {
                if (accept.nextElement().contains(candidateView.getContentType())) {
                    return candidateView;
                }
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.viewResolvers == null || this.viewResolvers.isEmpty()) {
            logger.warn("No ViewResolvers configured");
        }
    }

    public void setResolvers(List<ViewResolver> resolvers) {
        this.viewResolvers = resolvers;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
