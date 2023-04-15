package qrbillius.qrbill;

import net.codecrete.qrbill.generator.*;
import qrbillius.config.ProfileConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a few static methods that are used in a few different places.
 */
public class QRBillGenerator {

    private QRBillGenerator() {
    }

    public static List<Bill> createBills(List<QRBillInfo> infos, ProfileConfiguration config, GraphicsFormat format, OutputSize outputSize) {
        var res = new ArrayList<Bill>();

        for (var info : infos) {
            res.add(createBill(info, config, format, outputSize));
        }

        return res;
    }

    public static Bill createBill(QRBillInfo info, ProfileConfiguration config, GraphicsFormat format, OutputSize outputSize) {
        var bill = new Bill();

        // Creditor
        bill.setAccount(config.account());
        bill.setCreditor(config.address());

        // Bill Format
        bill.getFormat().setLanguage(config.language());
        bill.getFormat().setGraphicsFormat(format);
        bill.getFormat().setOutputSize(outputSize);

        // Amount
        bill.setAmount(parsePaymentAmount(info.getAmount()));
        bill.setCurrency("CHF");

        // Debtor
        bill.setDebtor(createAddress(info.getName(), info.getAddressLine1(), info.getAddressLine2()));

        // Unstructured Message
        bill.setUnstructuredMessage(info.getAdditionalInfo());

        return bill;
    }

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

        return new BigDecimal(str);
    }

    /**
     * Just creates an address with the given information and sets the country to "CH".
     */
    public static Address createAddress(String name, String addressLine1, String addressLine2) {
        return createAddress(name, addressLine1, addressLine2, "CH");
    }

    /**
     * Just creates an address with the given information.
     */
    public static Address createAddress(String name, String addressLine1, String addressLine2, String country) {
        var address = new Address();
        address.setName(name);
        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setCountryCode(country);
        return address;
    }

    /**
     * Checks whether the given string is a valid QRBill language.
     * Valid values are: DE, FR, IT, RM, EN
     * <p>
     * Note: RM is not official.
     */
    public static boolean isValidLanguage(String language) {
        try {
            Language.valueOf(language);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
