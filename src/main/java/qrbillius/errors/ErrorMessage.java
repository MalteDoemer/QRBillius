package qrbillius.errors;

import java.util.List;
import java.util.ResourceBundle;

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

    public String getFormattedMessage(ResourceBundle errorResources) {
        assert errorResources.containsKey(getMessageKey());
        if (formatParameters != null)
            return String.format(errorResources.getString(getMessageKey()), (Object[]) formatParameters);
        else
            return errorResources.getString(getMessageKey());
    }

    public String[] getFormatParameters() {
        return formatParameters;
    }

    public boolean hasFormatParameters() {
        return formatParameters != null;
    }
}
