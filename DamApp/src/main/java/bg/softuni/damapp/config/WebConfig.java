package bg.softuni.damapp.config;

import bg.softuni.damapp.interceptor.UserInterceptor;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    private final UserService userService;
    private final MessageService messageService;

    public WebConfig(LocaleChangeInterceptor localeChangeInterceptor, UserService userService, MessageService messageService) {
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(new UserInterceptor(userService, messageService));
    }
}
