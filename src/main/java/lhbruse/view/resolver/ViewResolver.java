package lhbruse.view.resolver;

import lhbruse.view.View;

/**
 * 视图解析器
 *
 * @author: LHB
 * @createDate: 2021/9/7
 * @version: 1.0
 */
public interface ViewResolver {

    /**
     * 通过视图名称解析视图
     */
    public View resolverViewName(String viewName) throws Exception;
}
