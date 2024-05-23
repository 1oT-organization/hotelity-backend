package org.iot.hotelitybackend.login.security;

import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.login.jwt.JwtFilter;
import org.iot.hotelitybackend.login.jwt.JwtUtil;
import org.iot.hotelitybackend.login.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public WebSecurityConfig(
            JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository,
            AuthenticationConfiguration authenticationConfiguration,
            EmployeeRepository employeeRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationConfiguration = authenticationConfiguration;
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        // form login disable
        http.formLogin(AbstractHttpConfigurer::disable);

        // http basic disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 인가(Authorization)
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()

                .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                /* 고객 */
                .requestMatchers(HttpMethod.POST, "/customers/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.DELETE, "/customers/**").hasAnyRole("ADMIN", "HM")

                /* 직원 */
                .requestMatchers(HttpMethod.POST, "/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/employees/**").hasRole("ADMIN")

                /* 호텔관리 */
                .requestMatchers(HttpMethod.POST, "/hotel-management/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.PUT, "/hotel-management/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.DELETE, "/hotel-management/**").hasAnyRole("ADMIN", "HM")

                /* 호텔서비스 */
                .requestMatchers(HttpMethod.POST, "/hotel-service/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.PUT, "/hotel-service/**").hasAnyRole("ADMIN", "HM")
                .requestMatchers(HttpMethod.DELETE, "/hotel-service/**").hasAnyRole("ADMIN", "HM")

                /* 마케팅 */
                .requestMatchers(HttpMethod.POST, "/marketing/**").hasAnyRole("ADMIN", "MS")
                .requestMatchers(HttpMethod.PUT, "/marketing/**").hasAnyRole("ADMIN", "MS")
                .requestMatchers(HttpMethod.DELETE, "/marketing/**").hasAnyRole("ADMIN", "MS")

                /* 영업관리 */
                .requestMatchers(HttpMethod.POST, "/sales/**").hasAnyRole("ADMIN", "MS")
                .requestMatchers(HttpMethod.PUT, "/sales/**").hasAnyRole("ADMIN", "MS")
                .requestMatchers(HttpMethod.DELETE, "/sales/**").hasAnyRole("ADMIN", "MS")

                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtFilter(jwtUtil, employeeRepository), AuthenticationFilter.class);

        AuthenticationFilter loginFilter = new AuthenticationFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository);

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        // 세션 설정
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
