package qrbillius;

import net.codecrete.qrbill.generator.Language;
import qrbillius.qrbill.Validator;

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

    private Properties properties;


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
        return properties.getProperty(key);
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
        var lang = getProperty(LANGUAGE);

        if (Validator.isValidLanguage(lang)) {
            return Language.valueOf(lang);
        } else {
            setProperty(LANGUAGE, "DE");
            return Language.DE;
        }
    }

    public void setLanguage(Language language) {
        setProperty(LANGUAGE, language.toString());
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