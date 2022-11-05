package qrbillius;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Settings;

public class SettingsProvider {
    public static Settings getNormalSettings() {
        var account = "CH4431999123000889012";
        var address = new Address();
        address.setName("Mr. Tester");
        address.setAddressLine1("Teststr. 69");
        address.setAddressLine2("4900 Langenthal");

        var language = Language.DE;
        var wordTemplate = "";
        var csvSeparator = ",";
        var nameFormat = "$1";
        var addressLine1Format = "$2";
        var addressLine2Format = "$3";
        var paymentAmountFormat = "$4";
        var additionalInfoFormat = "$5";

        return new Settings(
                account,
                address,
                language,
                wordTemplate,
                csvSeparator,
                nameFormat,
                addressLine1Format,
                addressLine2Format,
                paymentAmountFormat,
                additionalInfoFormat
        );
    }
}
