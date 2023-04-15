package qrbillius.errors;

/**
 * A simple Exception used to propagate an ErrorResult.
 */
public class ErrorResultException extends Exception {
    private final ErrorResult result;

    public ErrorResultException(ErrorResult result) {
        this.result = result;
    }

    public ErrorResult getResult() {
        return result;
    }
}
