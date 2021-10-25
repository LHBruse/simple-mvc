package lhbruse.view.resolver;

import lhbruse.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ViewResolver的实现基类。缓存已解析的视图对象,提高性能
 *
 * @author: LHB
 * @createDate: 2021/9/7
 * @version: 1.0
 */
public abstract class AbstractCachingViewResolver implements ViewResolver {

    ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 未能解析出视图时返回的空视图
     */
    private static final View UNRESOLVED_VIEW = (model, request, response) -> {
    };

    /**
     * 视图缓存
     */
    private Map<String, View> viewCache = new HashMap<>();

    @Override
    public View resolverViewName(String viewName) throws Exception {
        View view = viewCache.get(viewName);
        if (view != null) {
            return (view != UNRESOLVED_VIEW ? view : null);
        }
        reentrantLock.lock();
        try {
            view = viewCache.get(viewName);
            if (view != null) {
                return view != UNRESOLVED_VIEW ? view : null;
            }
            view = createView(viewName);
            if (view == null) {
                view = UNRESOLVED_VIEW;
            }
            viewCache.put(viewName, view);
            return view != UNRESOLVED_VIEW ? view : null;
        } finally {
            reentrantLock.unlock();
        }

    }

    /**
     * 创建实际的视图对象
     */
    protected abstract View createView(String viewName);
}
