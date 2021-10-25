package lhbruse.adapter;

import lhbruse.ModelAndView;
import lhbruse.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理器适配器
 *
 * @author: LHB
 * @createDate: 2021/9/6
 * @version: 1.0
 */
public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                        HandlerMethod handlerMethod) throws Exception;

}
