package qrbillius.errors;

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
