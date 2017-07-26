package com.kkn.masaran.security;

/**
 * Created by adhikasp on 08/10/2016.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/my-umkm/**")
                    .access("hasRole('ROLE_UMKM')")
                .antMatchers("/my-juragan/**")
                    .access("hasRole('ROLE_JURAGAN')")
                // Any other url is accessible by everyone
                .anyRequest()
                    .permitAll()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .successForwardUrl("/login/process")
                        .failureUrl("/login?error")
                        .permitAll()
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("remember-me")
                    .logoutSuccessUrl("/")
                .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("my-remember-me")
                    .tokenValiditySeconds(31536000);

    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler
    savedRequestAwareAuthenticationSuccessHandler() {

        SavedRequestAwareAuthenticationSuccessHandler auth
                = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        return auth;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from aw_user where username=?")
                .authoritiesByUsernameQuery("select username, role from user_role where username=?  ");
    }
}
