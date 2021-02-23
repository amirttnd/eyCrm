package com.zaloni.security.config;

import com.zaloni.security.component.OAuth2LoginSuccessHandler;
import com.zaloni.security.service.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    OAuth2UserService oAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .permitAll()
                .successHandler(oAuth2LoginSuccessHandler)
                .and()
                .logout().logoutSuccessUrl("/login").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
    }
}
