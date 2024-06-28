package example.com.auth.security;

import example.com.auth.jwt.JwtUtil;
import example.com.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
    private final EntryPointUnauthorizedHandler unauthorizedHandler = new EntryPointUnauthorizedHandler();
    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler = new JwtAuthenticationExceptionHandler();


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin((form) -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS));


        httpSecurity
                .authorizeHttpRequests(
                        (authorizeRequests) -> {
                            authorizeRequests
                                    .requestMatchers("/v1/auth/login").permitAll() // 로그인 엔드포인트 허용
                                    .requestMatchers("/v1/member/signin/{loginType}").permitAll()
                                    .requestMatchers("/v1/auth/reissue").permitAll()
                                    .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // 스웨거 관련 엔드포인트 허용
                                    .requestMatchers("/v1/**").hasAnyRole("MEMBER", "ADMIN")
                                    .requestMatchers("/admin/**").hasRole(RoleType.ADMIN.toString())
                                    .anyRequest().authenticated();

                        })
                .exceptionHandling(
                        (exceptionHandling) ->
                                exceptionHandling
                                        .accessDeniedHandler(accessDeniedHandler) // access deny 되었을 때 커스텀 응답 주기 위한 커스텀 handler
                                        .authenticationEntryPoint(unauthorizedHandler)) // 로그인되지 않은 요청에 대해 커스텀 응답 주기 위한 커스텀 handler
                .addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter에서 회원에 대한 탈퇴 여부 검증 진행
                .addFilterBefore(jwtAuthenticationExceptionHandler, JwtAuthFilter.class);


        return httpSecurity.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));  // 허용할 출처를 지정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
