package account.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> {
                    auth.antMatchers(
                            "/api/auth/signup",
                            "/api/auth/all",
                            "api/acct/**",
                            "api/admin/**").permitAll();
                    auth.antMatchers("/api/empl/payment","api/auth/changepass").authenticated();
                })
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
         */

        http.httpBasic()
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/actuator/shutdown").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/empl/payment/**", "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                .antMatchers(HttpMethod.POST, "/api/auth/changepass/**").hasAnyRole("ADMINISTRATOR", "USER", "ACCOUNTANT")
                .antMatchers(HttpMethod.POST, "/api/acct/payments/**", "/api/acct/payments").hasRole("ACCOUNTANT")
                .antMatchers(HttpMethod.PUT, "/api/acct/payments/**").hasRole("ACCOUNTANT")
                .antMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasRole("ADMINISTRATOR")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();

    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
