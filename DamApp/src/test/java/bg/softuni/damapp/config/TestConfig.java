package bg.softuni.damapp.config;

import bg.softuni.damapp.service.UserService;
import bg.softuni.damapp.validation.validators.UniqueEmailValidator;
import bg.softuni.damapp.web.rest.TestController;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    @Bean
    public TestController testController() {
        return new TestController();
    }

    @Bean
    public UniqueEmailValidator uniqueEmailValidator(UserService userService) {
        return new UniqueEmailValidator(userService);
    }

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
