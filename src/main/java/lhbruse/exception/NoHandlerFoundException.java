package lhbruse.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: LHB
 * @createDate: 2021/8/26
 * @version: 1.0
 */
public class NoHandlerFoundException extends ServletException {

    private final String httpMethod;

    private final String requestURL;

    public NoHandlerFoundException(HttpServletRequest request){
        this.httpMethod = request.getMethod();
        this.requestURL = request.getRequestURI();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
