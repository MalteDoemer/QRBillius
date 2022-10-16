package qrbillius.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorResult {

    private static final List<ErrorMessage> EMPTY_LIST = Collections.emptyList();

    private List<ErrorMessage> errors;

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
}
