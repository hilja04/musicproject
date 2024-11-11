package prjct2024.musicproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antMatcher("/css/**")).permitAll() // Static resources
                        .requestMatchers(antMatcher("/signup")).permitAll() // Signup page
                        .requestMatchers(antMatcher("/saveuser")).permitAll() // User registration
                        .requestMatchers(antMatcher("/songlist")).permitAll() // Song list page
                        .requestMatchers(antMatcher("/playlistlist")).permitAll() // Playlist list page
                        .requestMatchers(antMatcher("/view/**")).permitAll() // Viewing individual playlists
                        .requestMatchers(antMatcher("/playlists")).permitAll() // Playlists overview
                        .requestMatchers(antMatcher("/songs")).permitAll() // Songs page

                        // Login page (public access)
                        .requestMatchers(antMatcher("/login")).permitAll()

                        // H2 console (public access for development purposes)
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated() // Any other request requires authentication
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/playlistlist", true)
                        .permitAll())
                .logout(logout -> logout
                        .permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(antMatcher("/h2-console/**")) // Disable CSRF for H2 console
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Allow H2 console to be displayed in a frame
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}