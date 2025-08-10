package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    public static final String BASE_URL;
    public static final String TOKEN_URL;
    public static final String clientId;
    public static final String clientSecret;
    public  static final String api_token;


    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found in classpath");
            }
            properties.load(input);
            BASE_URL = properties.getProperty("baseUrl");
            if (BASE_URL == null || BASE_URL.isEmpty()) {
                throw new IllegalArgumentException("Base URL is not defined in config.properties");
            }
            TOKEN_URL = properties.getProperty("tokenUrl");
            if (TOKEN_URL == null || TOKEN_URL.isEmpty()) {
                throw new IllegalArgumentException("Token URL is not defined in config.properties");
            }
            clientId = properties.getProperty("clientId");
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("Client ID is not defined in config.properties");
            }
            clientSecret = properties.getProperty("clientSecret");
            if (clientSecret == null || clientSecret.isEmpty()) {
                throw new IllegalArgumentException("Client Secret is not defined in config.properties");
            }
            api_token = properties.getProperty("api_token");
            if (api_token == null || api_token.isEmpty()) {
                throw new IllegalArgumentException("API Token is not defined in config.properties");
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load config.properties: " + e.getMessage());
        }
    }
}