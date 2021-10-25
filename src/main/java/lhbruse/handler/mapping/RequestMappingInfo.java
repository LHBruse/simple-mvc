package lhbruse.handler.mapping;

import lhbruse.annotation.RequestMapping;
import lhbruse.http.RequestMethod;

import java.util.Objects;

/**
 * 请求路径映射信息，把配置{@link RequestMapping }注解的方法转换成{@link RequestMappingInfo}对象
 * @author: LHB
 * @createDate: 2021/8/19
 * @version: 1.0
 */
public class RequestMappingInfo {
    private String path;
    private RequestMethod requestMethod;

    /**
     * 用于注册handler
     */
    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.requestMethod = requestMapping.method();
    }

    /**
     * 用于请求
     */
    public RequestMappingInfo(String path, RequestMethod requestMethod) {
        this.path = path;
        this.requestMethod = requestMethod;
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestMappingInfo that = (RequestMappingInfo) o;
        return Objects.equals(path, that.path) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, requestMethod);
    }
}
