package lhbruse.exception;

import javax.servlet.ServletException;

/**
 * @author: LHB
 * @createDate: 2021/8/27
 * @version: 1.0
 */
public class MissingServletRequestParameterException extends ServletException {

    /**
     * 参数类型
     */
    private String parameterName;

    /**
     * 参数名称
     */
    private String parameterType;

    public MissingServletRequestParameterException(String parameterName, String parameterType) {
        super("Required " + parameterType + " parameter '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }
}
