package qrbillius.errors;

public class FormatSpecifierInvalidException extends Exception {
    private final String format;
    private final int pos;

    public FormatSpecifierInvalidException(String format, int pos) {
        this.format = format;
        this.pos = pos;
    }

    public String getFormat() {
        return format;
    }

    public int getPos() {
        return pos;
    }
}
