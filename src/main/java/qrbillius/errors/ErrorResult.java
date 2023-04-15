package qrbillius.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An ErrorResult contains zero or more ErrorMessages.
 * - If there were no errors the ErrorResult is empty.
 * - If any error occurred, it is stored in the ErrorResult.
 * ErrorResults may contain a line number to indicate the proper line number during imports.
 */
public class ErrorResult {

    private static final List<ErrorMessage> EMPTY_LIST = Collections.emptyList();

    public static final int NO_LINE_NUMBER = -1;

    private List<ErrorMessage> errors;

    private int lineNumber = NO_LINE_NUMBER;

    public ErrorResult() {
    }

    public List<ErrorMessage> getErrorMessages() {
        return errors == null ? EMPTY_LIST : errors;
    }

    public boolean hasErrors() {
        return errors != null;
    }

    public void addMessage(String key) {
        var message = new ErrorMessage(key);
        addMessage(message);
    }

    public void addMessage(String key, String... params) {
        var message = new ErrorMessage(key, params);
        addMessage(message);
    }

    public void addMessage(ErrorMessage message) {
        if (errors == null)
            errors = new ArrayList<>();
        errors.add(message);
    }

    public boolean hasLineNumber() {
        return lineNumber != NO_LINE_NUMBER;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
