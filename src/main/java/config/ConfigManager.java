package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties prop = new Properties();

    // Use a static initializer block to load the file automatically when the class loads
    static {
        try (InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config/qa.properties")) {

            if (inputStream == null) {
                throw new IOException("Could not find config/qa.properties");
            }
            prop.load(inputStream);
        } catch (IOException e) {
            // Static blocks cannot throw checked exceptions, so we wrap it
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static String getProperty(String key){
        // System.getProperty() can override configuration values (Enables CI/CD Flexibility)
        return System.getProperty(key, prop.getProperty(key));
    }

    public static String getBaseUrl(){
        return ConfigManager.getProperty("baseUrl");
    }

    public static String getApiBaseUrl(){
        return ConfigManager.getProperty("api.base.url");
    }

    public static String getAdminEmail(){
        return ConfigManager.getProperty("admin.email");
    }

    public static String getAdminPassword(){
        return ConfigManager.getProperty("admin.password");
    }

    public static String getDefaultEmail(){
        return ConfigManager.getProperty("default.customer.email");
    }

    public static String getDefaultPassword(){
        return ConfigManager.getProperty("default.customer.password");
    }

}
