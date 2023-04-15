package qrbillius.errors;

import java.util.List;
import java.util.ResourceBundle;

/**
 * This class represents a single error message composed of a language-neutral key
 * and possible format parameters used to construct the full error message.
 */
public class ErrorMessage {

    private final String messageKey;
    private final String[] formatParameters;

    public ErrorMessage(String messageKey) {
        this.messageKey = messageKey;
        this.formatParameters = null;
    }

    public ErrorMessage(String messageKey, String... formatParameters) {
        this.messageKey = messageKey;
        this.formatParameters = formatParameters;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public List<String> getFormatParameters() {
        return formatParameters != null ? List.of(formatParameters) : null;
    }

    public String getFormattedMessage(ResourceBundle errorResources) {
        assert errorResources.containsKey(getMessageKey());
        if (getFormatParameters() != null)
            return String.format(errorResources.getString(getMessageKey()), (Object[]) formatParameters);
        else
            return errorResources.getString(getMessageKey());
    }

    public boolean hasFormatParameters() {
        return formatParameters != null;
    }
}
