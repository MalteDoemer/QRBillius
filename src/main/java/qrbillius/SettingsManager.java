package qrbillius;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsManager {

    public final static String ACCOUNT = "Account";
    public final static String NAME = "Name";
    public final static String ADDRESS_LINE1 = "AddressLine1";
    public final static String ADDRESS_LINE2 = "AddressLine2";
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

        try(var writer = new FileWriter(configFile)) {
            properties.store(writer, "");
        }
     }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
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