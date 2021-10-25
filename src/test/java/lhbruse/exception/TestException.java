package lhbruse.exception;

/**
 * @author: LHB
 * @createDate: 2021/10/21
 * @version: 1.0
 */
public class TestException extends RuntimeException {
    private String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
