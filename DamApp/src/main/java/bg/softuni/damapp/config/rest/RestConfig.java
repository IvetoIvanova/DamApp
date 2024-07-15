package bg.softuni.damapp.config.rest;

import bg.softuni.damapp.config.AdvertisementApiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean("adsRestClient")
    public RestClient adsRestClient(AdvertisementApiConfig adApiConfig) {
        return RestClient
                .builder()
                .baseUrl(adApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
