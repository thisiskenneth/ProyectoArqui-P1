package ec.edu.espe.msauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Auth público
                .requestMatchers("/api/auth/**").permitAll()
                // CRUD completo de usuarios y roles — todos los métodos
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/roles/**").permitAll()
                // OpenAPI / Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // H2 Console (solo dev)
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
