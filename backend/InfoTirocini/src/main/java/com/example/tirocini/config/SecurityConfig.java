package com.example.tirocini.config;

import com.example.tirocini.model.Utente;
import com.example.tirocini.repository.UtenteRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UtenteRepository utenteRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Utente utente = utenteRepository.findByMail(username);
            if (utente == null) {
                throw new UsernameNotFoundException("Utente non trovato con email: " + username);
            }
            return utente;
        };
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String targetUrl = determineTargetUrl(authentication);
                redirectStrategy.sendRedirect(request, response, targetUrl);
            }

            protected String determineTargetUrl(Authentication authentication) {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities) {
                    if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                        return "/admin/dashboard"; 
                    }
                }
                return "/home"; 
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/accesso", "/registrazione", "/css/**", "/js/**", "/immagini/**", "/immagini-profilo/**").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
                .loginPage("/accesso")
                .loginProcessingUrl("/perform_login")
                .usernameParameter("mail")
                .successHandler(customAuthenticationSuccessHandler()) 
                .failureUrl("/accesso?error=true")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/accesso?logout=true")
                .permitAll()
        );

        return http.build();
    }
}