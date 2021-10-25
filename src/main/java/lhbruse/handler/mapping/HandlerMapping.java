package lhbruse.handler.mapping;

import lhbruse.handler.HandlerExecutionChain;
import javax.servlet.http.HttpServletRequest;
/**
 * 处理器映射器接口
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
public interface HandlerMapping {
    /**
     * 获取处理器执行链
     * @param request
     * @return
     * @throws Exception
     */
    HandlerExecutionChain getHandler(HttpServletRequest request)throws Exception;
}
