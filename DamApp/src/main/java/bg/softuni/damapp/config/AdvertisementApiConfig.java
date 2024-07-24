package bg.softuni.damapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ads.api")
public class AdvertisementApiConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public AdvertisementApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
