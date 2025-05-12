package mx.uv.daw.tienda.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** 1) PasswordEncoder para encriptar y verificar contraseñas */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 2) DaoAuthenticationProvider que usa tu UsuarioService + el encoder */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioService usuarioService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /** 3) Configuración de URLs, login, logout y permisos */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authProvider) throws Exception {
        http
                // Registrar nuestro provider
                .authenticationProvider(authProvider)

                // Definición de qué URLs van abiertas y cuáles protegidas
                .authorizeHttpRequests(auth -> auth
                        // Páginas públicas y recursos estáticos
                        .requestMatchers(
                                "/", "/index", "/tienda", "/register",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        // Rutas de administración → solo ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Rutas de carrito → solo CLIENTE
                        .requestMatchers("/carrito/**").hasRole("CLIENTE")

                        // Cualquier otra petición → CLIENTE
                        .anyRequest().hasRole("CLIENTE")
                )

                // Configuración del formulario de login
                .formLogin(form -> form
                        .loginPage("/login")               // Thymeleaf en /login
                        .loginProcessingUrl("/login")      // POST de credenciales
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")   // en caso de error vuelve con ?error
                        .permitAll()
                )

                // Configuración de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    /** 4) Redirección tras login según rol ADMIN o CLIENTE */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                // ADMIN → /admin/dashboard, CLIENTE → página pública /
                String target = isAdmin ? "/admin/dashboard" : "/";
                response.sendRedirect(request.getContextPath() + target);
            }
        };
    }
}
