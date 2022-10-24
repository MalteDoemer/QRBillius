package qrbillius.errors;

public class FormatSpecifierInvalidException extends FormatException {

    public FormatSpecifierInvalidException(String format, int pos) {
        super(format, pos);
    }

    @Override
    public ErrorResult createErrorResult() {
        var result = new ErrorResult();
        result.addMessage(new ErrorMessage(ErrorConstants.FORMAT_SPECIFIER_INVALID, getFormat()));
        return result;
    }
}
