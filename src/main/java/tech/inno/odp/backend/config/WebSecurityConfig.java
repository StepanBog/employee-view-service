package tech.inno.odp.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Profile("!integration-test")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    public static final String TOKEN_HEADER = "AccessToken";
//
//    private final UnauthorizedEntryPoint unauthorizedEntryPoint;
//    private final AuthenticationProvider preAuthenticatedJwtAuthenticationProvider;
//
//    @Value("${settings.security.cors.allowedOrigins:*}")
//    private String allowedOrigins;
//    @Value("${settings.security.cors.allowedMethods:*}")
//    private String allowedMethods;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(preAuthenticatedJwtAuthenticationProvider);
//    }
//
//    private CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList(allowedOrigins));
//        configuration.setAllowedMethods(Collections.singletonList(allowedMethods));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/auth",
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "/configuration/ui",
//                        "/swagger-resources/**",
//                        "/configuration/security",
//                        "/swagger-ui.html",
//                        "/swagger-ui/**",
//                        "/webjars/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
//                .addFilterBefore(jwtFilter(), AbstractPreAuthenticatedProcessingFilter.class)
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedEntryPoint);
//
//        http.csrf().disable();
//    }
//
//    @Bean
//    public JwtPreAuthenticatedProcessingFilter jwtFilter() throws Exception {
//        return new JwtPreAuthenticatedProcessingFilter(authenticationManager(), new NewJwtTokenSetAuthSuccessHandler());
//    }
}
