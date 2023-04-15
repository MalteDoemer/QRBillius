package qrbillius.config;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Application;
import qrbillius.qrbill.QRBillGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * This class manages access to the configuration file.
 */
public class ConfigurationManager {
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
    public final static String PAYMENT_AMOUNT_REQUIRED = "PaymentAmountRequired";
    public final static String ADDITIONAL_INFO_FORMAT = "AdditionalInfoFormat";
    public final static String ENABLE_PDF_TEMPLATE = "EnablePDFTemplate";
    public final static String PDF_TEMPLATE = "PDFTemplate";
    public final static String OPEN_PDF_WHEN_FINISHED = "OpenPDFWhenFinished";

    public final static String LAST_OPENED_FOLDER = "LastOpenedFolder";

    private ConfigurationManager() {
    }

    private record PropertiesWrapper(Properties properties) {
        private ApplicationConfiguration getApplicationConfiguration() {
            var lastOpenedFolder = getProperty(LAST_OPENED_FOLDER);
            return new ApplicationConfiguration(lastOpenedFolder);
        }

        private void setApplicationConfiguration(ApplicationConfiguration config) {
            setProperty(LAST_OPENED_FOLDER, config.lastOpenedFolder());
        }

        private ExportConfiguration getExportConfiguration() {
            var openPDFWhenFinished = getBooleanProperty(OPEN_PDF_WHEN_FINISHED);
            var enablePDFTemplate = getBooleanProperty(ENABLE_PDF_TEMPLATE);
            var pdfTemplate = getProperty(PDF_TEMPLATE);

            return new ExportConfiguration(
                    openPDFWhenFinished,
                    enablePDFTemplate,
                    pdfTemplate
            );
        }

        private void setExportConfiguration(ExportConfiguration config) {
            setBooleanProperty(OPEN_PDF_WHEN_FINISHED, config.openPDFWhenFinished());
            setBooleanProperty(ENABLE_PDF_TEMPLATE, config.enablePDFTemplate());
            setProperty(PDF_TEMPLATE, config.pdfTemplate());
        }

        private ImportConfiguration getImportConfiguration() {
            var csvSeparator = getProperty(CSV_SEPARATOR, ",");
            var nameFormat = getProperty(NAME_FORMAT);
            var addressLine1Format = getProperty(ADDRESS_LINE1_FORMAT);
            var addressLine2Format = getProperty(ADDRESS_LINE2_FORMAT);
            var paymentAmountFormat = getProperty(PAYMENT_AMOUNT_FORMAT);
            var paymentAmountRequired = getBooleanProperty(PAYMENT_AMOUNT_REQUIRED);
            var additionalInfoFormat = getProperty(ADDITIONAL_INFO_FORMAT);

            return new ImportConfiguration(
                    csvSeparator,
                    nameFormat,
                    addressLine1Format,
                    addressLine2Format,
                    paymentAmountFormat,
                    paymentAmountRequired,
                    additionalInfoFormat
            );
        }

        private void setImportConfiguration(ImportConfiguration config) {
            setProperty(CSV_SEPARATOR, config.csvSeparator());
            setProperty(NAME_FORMAT, config.nameFormat());
            setProperty(ADDRESS_LINE1_FORMAT, config.addressLine1Format());
            setProperty(ADDRESS_LINE2_FORMAT, config.addressLine2Format());
            setProperty(PAYMENT_AMOUNT_FORMAT, config.paymentAmountFormat());
            setBooleanProperty(PAYMENT_AMOUNT_REQUIRED, config.paymentAmountRequired());
            setProperty(ADDITIONAL_INFO_FORMAT, config.additionalInfoFormat());
        }

        private ProfileConfiguration getProfileConfiguration() {
            var account = getProperty(CREDITOR_ACCOUNT);
            var address = getCreditorAddress();
            var language = getLanguage();

            return new ProfileConfiguration(account, address, language);
        }

        private void setProfileConfiguration(ProfileConfiguration config) {
            setProperty(CREDITOR_ACCOUNT, config.account());
            setProperty(CREDITOR_NAME, config.address().getName());
            setProperty(CREDITOR_ADDRESS_LINE1, config.address().getAddressLine1());
            setProperty(CREDITOR_ADDRESS_LINE2, config.address().getAddressLine2());
            setProperty(LANGUAGE, config.language().name());
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

        private boolean getBooleanProperty(String key) {
            return Boolean.parseBoolean(getProperty(key));
        }

        private String getProperty(String key) {
            return getProperty(key, "");
        }

        private String getProperty(String key, String defaultValue) {
            return properties.getProperty(key, defaultValue);
        }

        private void setBooleanProperty(String key, boolean value) {
            setProperty(key, Boolean.toString(value));
        }

        private void setProperty(String key, String value) {
            properties.setProperty(key, value);
        }
    }

    /**
     * Load the application configuration from the config file.
     */
    public static ApplicationConfiguration loadApplicationConfiguration() throws IOException {
        var wrapper = loadPropertiesWrapper();
        return wrapper.getApplicationConfiguration();
    }

    /**
     * Write changes to the application configuration back to the config file.
     */
    public static void saveApplicationConfiguration(ApplicationConfiguration config) throws IOException {
        var wrapper = loadPropertiesWrapper();
        wrapper.setApplicationConfiguration(config);
        savePropertiesWrapper(wrapper);
    }

    /**
     * Load the import configuration from the config file.
     */
    public static ImportConfiguration loadImportConfiguration() throws IOException {
        var wrapper = loadPropertiesWrapper();
        return wrapper.getImportConfiguration();
    }

    /**
     * Write changes to the import configuration back to the config file.
     */
    public static void saveImportConfiguration(ImportConfiguration config) throws IOException {
        var wrapper = loadPropertiesWrapper();
        wrapper.setImportConfiguration(config);
        savePropertiesWrapper(wrapper);
    }

    /**
     * Load the export configuration from the config file.
     */
    public static ExportConfiguration loadExportConfiguration() throws IOException {
        var wrapper = loadPropertiesWrapper();
        return wrapper.getExportConfiguration();
    }

    /**
     * Write changes to the export configuration back to the config file.
     */
    public static void saveExportConfiguration(ExportConfiguration config) throws IOException {
        var wrapper = loadPropertiesWrapper();
        wrapper.setExportConfiguration(config);
        savePropertiesWrapper(wrapper);
    }

    /**
     * Load the profile configuration from the config file.
     */
    public static ProfileConfiguration loadProfileConfiguration() throws IOException {
        var wrapper = loadPropertiesWrapper();
        return wrapper.getProfileConfiguration();
    }

    /**
     * Write changes to the profile configuration back to the config file.
     */
    public static void saveProfileConfiguration(ProfileConfiguration config) throws IOException {
        var wrapper = loadPropertiesWrapper();
        wrapper.setProfileConfiguration(config);
        savePropertiesWrapper(wrapper);
    }


    private static PropertiesWrapper loadPropertiesWrapper() throws IOException {
        var properties = new Properties();
        var configFile = getConfigFile();

        if (configFile.exists()) {
            try (var reader = new FileReader(configFile)) {
                properties.load(reader);
            }
        }

        return new PropertiesWrapper(properties);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void savePropertiesWrapper(PropertiesWrapper wrapper) throws IOException {
        var configFile = getConfigFile();

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        try (var writer = new FileWriter(configFile)) {
            wrapper.properties.store(writer, "");
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
}
