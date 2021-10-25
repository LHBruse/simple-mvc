package lhbruse.handler.exception;

import lhbruse.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解决在处理程序映射或执行期间抛出的异常
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public interface HandlerExceptionResolver {

    /**
     * 尝试解决在处理程序执行期间引发的给定异常，返回表示特定错误页的ModelAndView。
     * 返回的ModelAndView可能为空，表示异常已成功解决，但不应呈现任何视图，而是通过设置状态代码。
     */
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex);
    
}
