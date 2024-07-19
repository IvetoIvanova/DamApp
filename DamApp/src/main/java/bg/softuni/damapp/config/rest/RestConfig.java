package bg.softuni.damapp.config.rest;

import bg.softuni.damapp.config.AdvertisementApiConfig;
import bg.softuni.damapp.service.JwtService;
import bg.softuni.damapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RestConfig {

    @Bean("adsRestClient")
    public RestClient adsRestClient(
            AdvertisementApiConfig adApiConfig,
            ClientHttpRequestInterceptor requestInterceptor) {
        return RestClient
                .builder()
                .baseUrl(adApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor requestInterceptor(
            UserService userService,
            JwtService jwtService) {
        return (request, body, execution) -> {
            userService
                    .getCurrentUser()
                    .ifPresent(damUserDetails -> {
                        String bearerToken = jwtService.generateToken(
                                damUserDetails.getUuid().toString(),
                                Map.of(
                                        "roles",
                                        damUserDetails.getAuthorities().stream()
                                                .map(GrantedAuthority::getAuthority)
                                                .toList(),
                                        "user",
                                        damUserDetails.getUuid().toString()
                                )
                        );
                        request.getHeaders().setBearerAuth(bearerToken);
                    });
            return execution.execute(request, body);
        };
    }
}
