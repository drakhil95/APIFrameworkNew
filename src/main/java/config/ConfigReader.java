package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader for loading environment-specific properties
 */
public class ConfigReader {

    private Properties properties;
    private String environment;

    public ConfigReader() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            // Get environment from system property or default to 'dev'
            environment = System.getProperty("environment", "dev");
            System.out.println("🔧 Loading configuration for environment: " + environment);

            // Load properties file
            properties = new Properties();
            String configFile = "src/test/resources/config/" + environment + ".properties";

            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
                System.out.println("✅ Configuration loaded successfully");
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to load configuration: " + e.getMessage());
            // Load from classpath as fallback
            loadFromClasspath();
        }
         
    }

    private void loadFromClasspath() {
        try {
            String configFile = "/config/" + environment + ".properties";
            properties.load(getClass().getResourceAsStream(configFile));
            System.out.println("✅ Configuration loaded from classpath");
        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    // Configuration getters
    public String getEnvironment() { return environment; }
    public String getBaseUrl() { return properties.getProperty("base.url"); }
    public String getBasePath() { return properties.getProperty("base.path", ""); }
    public int getPort() { return Integer.parseInt(properties.getProperty("port", "443")); }
    public long getRequestTimeout() { return Long.parseLong(properties.getProperty("request.timeout", "5000")); }
    public long getConnectionTimeout() { return Long.parseLong(properties.getProperty("connection.timeout", "10000")); }
    public String getReportsName() { return properties.getProperty("reports.name", "API Test Report"); }
    public int getThreadCount() { return Integer.parseInt(properties.getProperty("thread.count", "2")); }
    public String getDbUrl() { return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/testdb"); }
    public String getDbUsername() { return properties.getProperty("db.username", "root"); }
    public String getDbPassword() { return properties.getProperty("db.password", "password"); }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}