package mx.uv.daw.tienda.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.uv.daw.tienda.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration//avisa a Spring que la clase es de configuraciones
@EnableWebSecurity//seguridad de web de Sprfin
@EnableMethodSecurity(prePostEnabled = true)//permite usar anotaciones de seguridad en metodos
public class SecurityConfig {

    /** 1) PasswordEncoder para encriptar y verificar contraseñas */
    @Bean
    public PasswordEncoder passwordEncoder() { //metodoBEAN para crear un codificador de contraseña usuando el BCrypt

        return new BCryptPasswordEncoder();
    }

    /** 2) DaoAuthenticationProvider que usa tu UsuarioService + el encoder */
    @Bean//puente entre Spring Security y nuestra lógica de  (usuarioService)
    // Bean que conecta la lógica de usuarios con Spring Security
// Utiliza UsuarioService para buscar usuarios y compara contraseñas cifradas
    public DaoAuthenticationProvider authenticationProvider(UsuarioService usuarioService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /** 3) Configuración de URLs, login, logout y permisos */
    @Bean//Define qué rutas son públicas y cuáles requieren autenticación
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authProvider) throws Exception {
        http
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> auth
                        //rutas accesibles sin autenticacion
                        .requestMatchers("/", "/index", "/tienda", "/register", "/login",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/producto/**")
                        .permitAll()
                        //rutas admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //rutas cliente - permitir acceso a ADMIN también para poder redirigir en el controlador
                        .requestMatchers("/carrito/**", "/perfil/**", "/pedido/**").hasAnyRole("CLIENTE", "ADMIN")
                        //cualquier otra ruta necesita autenticacion
                        .anyRequest().authenticated()
                )
                //form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                //defince la URL para cerrar la sesion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                // Maneja excepciones de seguridad (como CSRF desactivado en recursos estáticos)( CSRP: Falsificación de Petición en Sitios Cruzados.)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**")
                        .ignoringRequestMatchers("/admin/perfil/editar")
                )
                // Manejo de excepciones de acceso denegado
                .exceptionHandling(exc -> exc
                        .accessDeniedPage("/admin/dashboard")
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

                String target = isAdmin ? "/admin/dashboard" : "/perfil";
                response.sendRedirect(request.getContextPath() + target);
            }
        };
    }
}
