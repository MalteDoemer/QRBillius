package qrbillius.qrbill;

import java.util.List;

/**
 * This interface represents a class that can iterate over the rows of a table and indicate the current line number.
 */
public interface InputParser extends Iterable<List<String>>, AutoCloseable {
    long getCurrentLineNumber();
}