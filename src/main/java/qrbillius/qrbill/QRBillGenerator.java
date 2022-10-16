package qrbillius.qrbill;

import net.codecrete.qrbill.generator.Language;

import java.math.BigDecimal;

public class QRBillGenerator {


    /**
     * Parses the payment amount field, replaces '-' with zeros and removes 'CHF' or 'Fr.' prefixes.
     *
     * @return The payment amount, or null if the string was null or blank
     */
    public static BigDecimal parsePaymentAmount(String amount) {
        // payment amount is optional
        if (amount == null || amount.isBlank())
            return null;

        // if there is a '-' character in the string it means a 0 e.g. 'Fr. 5.-' would be 'Fr. 5.0'
        var str = amount.replace('-', '0').strip();

        // the payment amount can start with either 'Fr.' or 'CHF'
        if (str.startsWith("Fr.")) {
            str = str.substring(3).strip();
        } else if (str.startsWith("CHF")) {
            str = str.substring(3).strip();
        }

        return new BigDecimal(amount);
    }

    public static boolean isValidLanguage(String language) {
        try {
            Language.valueOf(language);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
