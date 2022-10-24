package qrbillius.errors;

public class FormatOutOfBoundsException extends Exception {
    private final String format;
    private final int pos;
    private final int index;

    public FormatOutOfBoundsException(String format, int pos, int index) {
        this.format = format;
        this.pos = pos;

        this.index = index;
    }

    public String getFormat() {
        return format;
    }

    public int getPos() {
        return pos;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getMessage() {
        return String.format("The format index %d is out of bounds", index);
    }
}
