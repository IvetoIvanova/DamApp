package bg.softuni.damapp.config;

import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.impl.DamUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.remembered.key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/", "/login", "/register", "/list-advertisements").permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                )
                .rememberMe(rememberMe ->
                                rememberMe
                                        .key(rememberMeKey)
                                        .rememberMeParameter("remember-me")
                                        .rememberMeCookieName("remember-me")
                                        .tokenValiditySeconds(7 * 24 * 60 * 60)
//                                .useSecureCookie(true)
                )
                .logout(
                        logout ->
                                logout
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/")
                                        .invalidateHttpSession(true)
                                        .deleteCookies("JSESSIONID")
                                        .permitAll()
                )
                .build();
    }


    @Bean
    public DamUserDetailsService userDetailsService(UserRepository userRepository) {
        return new DamUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder
                .defaultsForSpringSecurity_v5_8();
    }

}

