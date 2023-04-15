package qrbillius.qrbill;

import qrbillius.errors.FormatException;
import qrbillius.errors.FormatOutOfBoundsException;
import qrbillius.errors.FormatSpecifierInvalidException;

import java.util.List;


/**
 * The FormatParser.<p>
 * - The FormatParser is created with a format string for exactly one field (i.e. name or addressLine1)<p>
 * - It can then be used on a "row" of data to read the value for that field.<p>
 * - The FormatParser can either be 0 or 1 indexed. (default 1-indexed)
 * Example:
 * Consider a table with three columns like this:
 * <p>
 * - Potter, Harry, Fr. 25 <p>
 * - Hermione, Hermione, Fr. 32 <p>
 * - Weasley, Ronald, Fr. 5 <p>
 * <p>
 * And a format specifier for the field name like $2 $1.
 * <p>
 * You could create a FormatParser with this specifier and iterate over all rows
 * and call the parse function for each to get the following names:<p>
 * - Harry Potter <p>
 * - Hermione Granger <p>
 * - Ronald Weasley <p>
 */
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
    public String parse(List<String> record) throws FormatException {
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

            var parsedNumber = Integer.parseInt(formatString.substring(i + 1, j));
            var index = parsedNumber;

            if (!isZeroIndexed()) {
                index = parsedNumber - 1;
            }

            if (index >= record.size()) {
                throw new FormatOutOfBoundsException(formatString, i, parsedNumber);
            } else {
                builder.append(record.get(index));
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
