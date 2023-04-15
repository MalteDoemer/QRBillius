package qrbillius.errors;

/**
 * A simple Exception used to signal an error while applying the input format.
 */
public abstract class FormatException extends Exception {
    protected final String format;
    protected final int pos;

    protected FormatException(String format, int pos) {
        this.format = format;
        this.pos = pos;
    }

    public String getFormat() {
        return format;
    }

    public int getPos() {
        return pos;
    }

    public abstract ErrorResult createErrorResult();
}
