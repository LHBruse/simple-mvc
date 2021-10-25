package lhbruse.vo;

/**
 * @author: LHB
 * @createDate: 2021/10/21
 * @version: 1.0
 */
public class ApiResponse {
    private int code;
    private String message;
    private String data;

    public ApiResponse(int code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
