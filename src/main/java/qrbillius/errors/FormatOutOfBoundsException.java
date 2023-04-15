package qrbillius.errors;

/**
 * This Exception is used to signal that a format specifier that was greater than the max amount of columns has been used.
 * For example if the input contains 3 columns and the format contains $4 this error occurs.
 */
public class FormatOutOfBoundsException extends FormatException {
    private final int index;

    public FormatOutOfBoundsException(String format, int pos, int index) {
        super(format, pos);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public ErrorResult createErrorResult() {
        var result = new ErrorResult();
        result.addMessage(new ErrorMessage(ErrorConstants.FORMAT_SPECIFIER_OUT_OF_BOUNDS, "$" + getIndex()));
        return result;
    }
}
