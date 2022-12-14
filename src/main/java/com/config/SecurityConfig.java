package com.config;

import com.models.Role;
import com.services.PersonDetailsService;
import com.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                antMatchers("/booksForLibrarian").hasRole(Role.LIBRARIAN.name()).
                antMatchers("/admin").hasRole(Role.ADMIN.name()).
                antMatchers("/booksForReader").hasRole(Role.READER.name()).
                antMatchers("/auth/login","/auth/registration","/error").permitAll().
                anyRequest().hasAnyRole(Role.READER.name(),Role.LIBRARIAN.name(),Role.ADMIN.name()).
                and().
                formLogin().loginPage("/auth/login").
                loginProcessingUrl("/process_login").
                defaultSuccessUrl("/",true).
                usernameParameter("username").passwordParameter("password").
                failureUrl("/auth/login?error").
                and().
                logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
