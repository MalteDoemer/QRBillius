package qrbillius.qrbill;

import qrbillius.errors.FormatOutOfBoundsException;
import qrbillius.errors.FormatSpecifierInvalidException;

import java.util.List;

public class FormatParser {
    /**
     * Indicates whether the parser should index starting at 0 or at 1
     */
    private boolean zeroIndexed = false;

    private final String formatString;

    private final StringBuilder builder;

    public FormatParser(String formatString) {
        this.formatString = formatString;
        this.builder = new StringBuilder();
    }


    /**
     * This function parses the formatString and replaces the placeholders (a dollar followed by a number)
     * with the appropriate string from the record.
     */
    public String parse(List<String> record) throws FormatSpecifierInvalidException, FormatOutOfBoundsException {
        builder.setLength(0);

        for (int i = 0; i < formatString.length(); i++) {
            var current = formatString.charAt(i);

            if (current != '$') {
                builder.append(current);
                continue;
            } else if (i == formatString.length() - 1) {
                throw new FormatSpecifierInvalidException(formatString, i);
            }

            var next = formatString.charAt(i + 1);

            if (next == '$') {
                builder.append('$');
            } else if (!Character.isDigit(next)) {
                throw new FormatSpecifierInvalidException(formatString, i);
            }

            int j = i + 1;
            while (j < formatString.length() && Character.isDigit(formatString.charAt(j))) {
                j++;
            }

            var number = Integer.parseInt(formatString.substring(i + 1, j));

            if (!isZeroIndexed()) {
                number = number - 1;
            }

            if (number >= record.size()) {
                throw new FormatOutOfBoundsException(formatString, i, number);
            } else {
                builder.append(record.get(number));
            }

            i = j - 1; // skip to the end of the number
        }

        return builder.toString();
    }


    public boolean isZeroIndexed() {
        return zeroIndexed;
    }

    public void setZeroIndexed(boolean zeroIndexed) {
        this.zeroIndexed = zeroIndexed;
    }
}
