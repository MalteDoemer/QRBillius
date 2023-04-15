package qrbillius.errors;

/**
 * This Exception is used to signal a syntax error in the format specifier.
 * This could be for example a dollar not followed by a number: $A
 */
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
