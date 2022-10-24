package qrbillius.errors;

public class ErrorResultException extends Exception {
    private final ErrorResult result;

    public ErrorResultException(ErrorResult result) {
        this.result = result;
    }

    public ErrorResult getResult() {
        return result;
    }
}
