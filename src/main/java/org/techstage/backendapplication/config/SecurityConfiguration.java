    package org.techstage.backendapplication.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.web.servlet.config.annotation.CorsRegistry;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    public class SecurityConfiguration implements WebMvcConfigurer {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((authz) -> {
                        authz
                                .anyRequest().permitAll();
                    })
                    .formLogin((form) ->
                            form.loginPage("/login")
                                    .permitAll()
                    )
                    .logout((logout) -> logout
                            .logoutUrl("/logout")
                            .permitAll())
                    .httpBasic(Customizer.withDefaults())
                    .csrf().disable();
            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            var user =
                    User.withDefaultPasswordEncoder()
                            .username("username")
                            .password("password")
                            .authorities("ROLE_USER")
                            .build();

            return new InMemoryUserDetailsManager(user);
        }
    }