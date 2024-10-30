package ar.com.tubarberia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadWeb {
@Bean
public BCryptPasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    .authorizeHttpRequests((authorize) -> authorize
    .requestMatchers("/admin/").hasRole("ADMIN")
    .requestMatchers("/proveedores/**").permitAll()
    .requestMatchers("/css/", "/js/", "/img/", "/**")
    .permitAll()
    )

    .formLogin((form) -> form
    .loginPage("/login")
    .loginProcessingUrl("/logincheck")
    .usernameParameter("email")
    .passwordParameter("password")
    .defaultSuccessUrl("/redirectByRole", true)
    .permitAll()
    )
    .logout((logout) -> logout
    .logoutUrl("/logout")
    .logoutSuccessUrl("/")
    .permitAll()
    )
    .csrf(csrf -> csrf.disable());
    return http.build();
    }
}
