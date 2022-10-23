package qrbillius;

import net.codecrete.qrbill.generator.Language;
import qrbillius.qrbill.CSVConfig;
import qrbillius.qrbill.QRBillConfig;
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
    public final static String CSV_NAME_FORMAT = "CSVNameFormat";
    public final static String CSV_ADDRESS_LINE1_FORMAT = "CSVAddressLine1Format";
    public final static String CSV_ADDRESS_LINE2_FORMAT = "CSVAddressLine2Format";
    public final static String CSV_PAYMENT_AMOUNT_FORMAT = "CSVPaymentAmountFormat";
    public final static String CSV_ADDITIONAL_INFO_FORMAT = "CSVAdditionalInfoFormat";


    private final Properties properties;


    public SettingsManager() {
        properties = new Properties();
    }

    public void load() throws IOException {
        var configFile = getConfigFile();

        if (configFile.exists()) {
            try (var reader = new FileReader(configFile)) {
                properties.load(reader);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save() throws IOException {
        var configFile = getConfigFile();

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        try (var writer = new FileWriter(configFile)) {
            properties.store(writer, "");
        }
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getCreditorAccount() {
        return getProperty(CREDITOR_ACCOUNT);
    }

    public void setCreditorAccount(String creditorAccount) {
        setProperty(CREDITOR_ACCOUNT, creditorAccount);
    }

    public String getCreditorName() {
        return getProperty(CREDITOR_NAME);
    }

    public void setCreditorName(String creditorName) {
        setProperty(CREDITOR_NAME, creditorName);
    }

    public String getCreditorAddressLine1() {
        return getProperty(CREDITOR_ADDRESS_LINE1);
    }

    public void setCreditorAddressLine1(String creditorAddressLine1) {
        setProperty(CREDITOR_ADDRESS_LINE1, creditorAddressLine1);
    }

    public String getCreditorAddressLine2() {
        return getProperty(CREDITOR_ADDRESS_LINE2);
    }

    public void setCreditorAddressLine2(String creditorAddressLine2) {
        setProperty(CREDITOR_ADDRESS_LINE2, creditorAddressLine2);
    }

    public Language getLanguage() {
        var lang = getProperty(LANGUAGE, "DE");

        if (QRBillGenerator.isValidLanguage(lang)) {
            return Language.valueOf(lang);
        } else {
            setProperty(LANGUAGE, "DE");
            return Language.DE;
        }
    }

    public void setLanguage(Language language) {
        setProperty(LANGUAGE, language.toString());
    }

    public QRBillConfig getQRBillConfig() {
        var account = getCreditorAccount();
        var address = QRBillGenerator.createAddress(getCreditorName(), getCreditorAddressLine1(), getCreditorAddressLine2());
        return new QRBillConfig(account, address, getLanguage());
    }

    public String getCsvSeparator() {
        return properties.getProperty(CSV_SEPARATOR, ",");
    }

    public void setCsvSeparator(String csvSeparator) {
        properties.setProperty(CSV_SEPARATOR, csvSeparator);
    }

    public String getCsvNameFormat() {
        return properties.getProperty(CSV_NAME_FORMAT, "$1");
    }

    public void setCsvNameFormat(String csvNameFormat) {
        properties.setProperty(CSV_NAME_FORMAT, csvNameFormat);
    }

    public String getCsvAddressLine1Format() {
        return properties.getProperty(CSV_ADDRESS_LINE1_FORMAT, "$2");
    }

    public void setCsvAddressLine1Format(String csvAddressLine1Format) {
        properties.setProperty(CSV_ADDRESS_LINE1_FORMAT, csvAddressLine1Format);
    }

    public String getCsvAddressLine2Format() {
        return properties.getProperty(CSV_ADDRESS_LINE2_FORMAT, "$3");
    }

    public void setCsvAddressLine2Format(String csvAddressLine2Format) {
        properties.setProperty(CSV_ADDRESS_LINE2_FORMAT, csvAddressLine2Format);
    }

    public String getCsvPaymentAmountFormat() {
        return properties.getProperty(CSV_PAYMENT_AMOUNT_FORMAT, "$4");
    }

    public void setCsvPaymentAmountFormat(String csvPaymentAmountFormat) {
        properties.setProperty(CSV_PAYMENT_AMOUNT_FORMAT, csvPaymentAmountFormat);
    }

    public String getCsvAdditionalInfoFormat() {
        return properties.getProperty(CSV_ADDITIONAL_INFO_FORMAT, "$5");
    }

    public void setCsvAdditionalInfoFormat(String csvAdditionalInfoFormat) {
        properties.setProperty(CSV_ADDITIONAL_INFO_FORMAT, csvAdditionalInfoFormat);
    }

    public CSVConfig getCsvConfig() {
        return new CSVConfig(
                getCsvSeparator(),
                getCsvNameFormat(),
                getCsvAddressLine1Format(),
                getCsvAddressLine2Format(),
                getCsvPaymentAmountFormat(),
                getCsvAdditionalInfoFormat()
        );
    }

    private File getConfigFile() throws IOException {
        var userDir = System.getProperty("user.home");

        if (userDir == null) {
            throw new IOException("user.home not found");
        }

        var dir = Paths.get(userDir, ".qrbill").toFile();
        return Paths.get(dir.toString(), "qrbill.properties").toFile();
    }
}