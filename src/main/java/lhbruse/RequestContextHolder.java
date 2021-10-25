package lhbruse;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: LHB
 * @createDate: 2021/9/8
 * @version: 1.0
 */
public class RequestContextHolder {

    private static final ThreadLocal<HttpServletRequest> inheritableRequestHolder = new NamedInheritableThreadLocal<>("Request context");

    public static void resetRequest() {
        inheritableRequestHolder.remove();
    }

    public static HttpServletRequest getRequest() {
        return inheritableRequestHolder.get();
    }

    public static void setRequest(HttpServletRequest request) {
        inheritableRequestHolder.set(request);
    }

}
