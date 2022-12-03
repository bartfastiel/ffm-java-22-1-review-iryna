package de.neuefische.booklibrary.backend;

import de.neuefische.booklibrary.backend.security.AppUser;
import de.neuefische.booklibrary.backend.security.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppUserService appUserService;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder encoder() {
        return passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/api/app-users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/me").authenticated()
                .antMatchers(HttpMethod.GET, "/api/app-users/role").authenticated()
                .antMatchers(HttpMethod.GET, "/api/app-users/logout").authenticated()
                .antMatchers(HttpMethod.POST, "/api/app-users/member").permitAll()
                .antMatchers(HttpMethod.POST, "/api/app-users/librarian").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/app-users/**").authenticated()


                .antMatchers(HttpMethod.GET, "/api/books").permitAll()
                .antMatchers(HttpMethod.GET, "/search/**").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.GET, "/isbn/**").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.POST, "/api/books").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("LIBRARIAN")

                .anyRequest().denyAll()
                .and().build();
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        return new UserDetailsManager() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUserFromDB = appUserService.findByUsername(username);
                if (appUserFromDB == null) {
                    throw new UsernameNotFoundException("Username not found");
                }
                return User.builder()
                        .username(username)
                        .password(appUserFromDB.password())
                        .roles(appUserFromDB.role().toString())
                        .build();
            }

            @Override
            public void createUser(UserDetails user) {

            }

            @Override
            public void updateUser(UserDetails user) {

            }

            @Override
            public void deleteUser(String username) {

            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {

            }

            @Override
            public boolean userExists(String username) {
                return false;
            }

        };
    }

}
