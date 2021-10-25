package lhbruse.http;

/**
 * HTTP状态代码的枚举
 * @author: LHB
 * @createDate: 2021/8/26
 * @version: 1.0
 */
public enum HttpStatus {

    /**
     * 200 OK.
     */
    OK(200, "OK");

    private final int value;

    private final String reasonPhrase;

    HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
