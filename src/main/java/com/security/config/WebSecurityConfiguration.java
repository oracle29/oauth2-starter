package com.security.config;

import com.security.exception.Oauth2ExceptionTranslator;
import com.security.oauthfilter.Oauth2InfoExceptionFilter;
import com.security.oauthfilter.Oauth2ParamsValidateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;

/**
 * Created by 陈圣融 on 2017-07-23.
 */
@Order(-5)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous().disable()
                .requestMatchers().antMatchers("/oauth/**").and()
                .addFilterBefore(clientCredentialsTokenEndpointFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new Oauth2ParamsValidateFilter(), ClientCredentialsTokenEndpointFilter.class)
                .addFilterAfter(new Oauth2InfoExceptionFilter(), ExceptionTranslationFilter.class);
    }



    /**
     * oauth2 token获取过滤器
     *
     * @return
     */
    @Bean
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter() {
        ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter();
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new ClientDetailsUserDetailsService(clientDetailsService));
        authenticationProvider.setHideUserNotFoundExceptions(false);
        AuthenticationManager authenticationManager = new ProviderManager(Arrays.<AuthenticationProvider>asList(authenticationProvider));
        try {
            clientCredentialsTokenEndpointFilter.setAuthenticationEntryPoint(new Oauth2ExceptionTranslator());
            clientCredentialsTokenEndpointFilter.setAuthenticationManager(authenticationManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientCredentialsTokenEndpointFilter;
    }


}
