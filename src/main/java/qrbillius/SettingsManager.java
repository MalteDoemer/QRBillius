package qrbillius;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;
import qrbillius.qrbill.QRBillGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsManager {

    public final static String CREDITOR_ACCOUNT = "Account";
    public final static String CREDITOR_NAME = "Name";
    public final static String CREDITOR_ADDRESS_LINE1 = "AddressLine1";
    public final static String CREDITOR_ADDRESS_LINE2 = "AddressLine2";
    public final static String LANGUAGE = "Language";

    public final static String CSV_SEPARATOR = "CSVSeparator";
    public final static String NAME_FORMAT = "NameFormat";
    public final static String ADDRESS_LINE1_FORMAT = "AddressLine1Format";
    public final static String ADDRESS_LINE2_FORMAT = "AddressLine2Format";
    public final static String PAYMENT_AMOUNT_FORMAT = "PaymentAmountFormat";
    public final static String ADDITIONAL_INFO_FORMAT = "AdditionalInfoFormat";

    private final Properties properties;

    private SettingsManager(Properties properties) {
        this.properties = properties;
    }

    public static Settings load() throws IOException {
        var properties = new Properties();
        var configFile = getConfigFile();

        if (configFile.exists()) {
            try (var reader = new FileReader(configFile)) {
                properties.load(reader);
            }
        }

        var manager = new SettingsManager(properties);
        return manager.getSettings();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save(Settings settings) throws IOException {
        var properties = new Properties();
        var manager = new SettingsManager(properties);
        manager.setSettings(settings);

        var configFile = getConfigFile();

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        try (var writer = new FileWriter(configFile)) {
            properties.store(writer, "");
        }
    }

    private static File getConfigFile() throws IOException {
        var userDir = System.getProperty("user.home");

        if (userDir == null) {
            throw new IOException("user.home not found");
        }

        var dir = Paths.get(userDir, ".qrbillius").toFile();
        return Paths.get(dir.toString(), "qrbillius.properties").toFile();
    }

    private Settings getSettings() {
        String account = getProperty(CREDITOR_ACCOUNT);
        Address address = getCreditorAddress();
        Language language = getLanguage();
        String csvSeparator = getProperty(CSV_SEPARATOR);
        String nameFormat = getProperty(NAME_FORMAT);
        String addressLine1Format = getProperty(ADDRESS_LINE1_FORMAT);
        String addressLine2Format = getProperty(ADDRESS_LINE2_FORMAT);
        String paymentAmountFormat = getProperty(PAYMENT_AMOUNT_FORMAT);
        String additionalInfoFormat = getProperty(ADDITIONAL_INFO_FORMAT);

        return new Settings(
                account,
                address,
                language,
                csvSeparator,
                nameFormat,
                addressLine1Format,
                addressLine2Format,
                paymentAmountFormat,
                additionalInfoFormat
        );
    }

    private void setSettings(Settings settings) {
        setProperty(CREDITOR_ACCOUNT, settings.account());
        setProperty(CREDITOR_NAME, settings.address().getName());
        setProperty(CREDITOR_ADDRESS_LINE1, settings.address().getAddressLine1());
        setProperty(CREDITOR_ADDRESS_LINE2, settings.address().getAddressLine2());
        setProperty(LANGUAGE, settings.language().name());
        setProperty(CSV_SEPARATOR, settings.csvSeparator());
        setProperty(NAME_FORMAT, settings.nameFormat());
        setProperty(ADDRESS_LINE1_FORMAT, settings.addressLine1Format());
        setProperty(ADDRESS_LINE2_FORMAT, settings.addressLine2Format());
        setProperty(PAYMENT_AMOUNT_FORMAT, settings.paymentAmountFormat());
        setProperty(ADDITIONAL_INFO_FORMAT, settings.additionalInfoFormat());
    }

    private Address getCreditorAddress() {
        var name = getProperty(CREDITOR_NAME);
        var addressLine1 = getProperty(CREDITOR_ADDRESS_LINE1);
        var addressLine2 = getProperty(CREDITOR_ADDRESS_LINE2);
        return QRBillGenerator.createAddress(name, addressLine1, addressLine2);
    }

    private Language getLanguage() {
        var lang = getProperty(LANGUAGE, "DE");

        if (QRBillGenerator.isValidLanguage(lang)) {
            return Language.valueOf(lang);
        } else {
            setProperty(LANGUAGE, "DE");
            return Language.DE;
        }
    }

    private String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    private String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}