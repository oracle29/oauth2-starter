package com.security.config;

import com.security.exception.Oauth2ExceptionTranslator;
import com.security.manager.CustomJdbcUserDetailsManager;
import com.security.service.OauthTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * Created by 陈圣融 on 2017-07-23.
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager auth;
    //    public BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    DataSource dataSource;
    @Autowired
    RedisConnectionFactory connectionFactory;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
//        return new InMemoryTokenStore();
    }

    @Autowired
    ClientDetailsService clientDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        if (auth instanceof ProviderManager) {
            ProviderManager auth = (ProviderManager) this.auth;
            for (AuthenticationProvider authenticationProvider : auth.getProviders()) {
                if (authenticationProvider instanceof DaoAuthenticationProvider) {
                    ((DaoAuthenticationProvider) authenticationProvider).setHideUserNotFoundExceptions(false);
                }
            }
        }
        endpoints
                .authenticationManager(auth)
                .tokenStore(tokenStore())
                .allowedTokenEndpointRequestMethods(new HttpMethod[]{HttpMethod.GET, HttpMethod.POST})
                .exceptionTranslator(new Oauth2ExceptionTranslator())
                .tokenServices(oauthTokenServices());
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices oauthTokenServices() {
        OauthTokenServices oauthTokenServices = new OauthTokenServices();
        oauthTokenServices.setTokenStore(tokenStore());
        oauthTokenServices.setSupportRefreshToken(true);
        oauthTokenServices.setReuseRefreshToken(true);
        oauthTokenServices.setClientDetailsService(clientDetailsService);
        return oauthTokenServices;
    }

    //oauth2 client用户初始化，可以直接操作实体类注册用户
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {

//        clients.inMemory()
//                .withClient("client")
//                .secret("secret")
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(20) //  20 seconds
//                .refreshTokenValiditySeconds(2592000) // 30 days
//                .and()
//                .withClient("svca-service")
//                .secret("password")
//                .authorizedGrantTypes("client_credentials", "refresh_token")
//                .scopes("server")
//                .and()
//                .withClient("svcb-service")
//                .secret("password")
//                .authorizedGrantTypes("client_credentials", "refresh_token")
//                .scopes("server");

        clients.jdbc(dataSource);

     /*   clients.jdbc(dataSource)
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds(20) // 20 seconds
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("svca-service")
                .secret("password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .and()
                .withClient("svcb-service")
                .secret("password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server");*/
    }


    @Configuration
    @Order(-20)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Autowired
        DataSource dataSource;

        @Bean
        public CustomJdbcUserDetailsManager customJdbcUserDetailsManager() {
            CustomJdbcUserDetailsManager customJdbcUserDetailsManager = new CustomJdbcUserDetailsManager();
            customJdbcUserDetailsManager.setDataSource(dataSource);
            return customJdbcUserDetailsManager;
        }

        //spring security 用户初始化
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication()
//                    .withUser("dave").password("secret").roles("USER")
//                    .and()
//                    .withUser("anil").password("password").roles("ADMIN");
/*            CustomJdbcUserDetailsManagerConfigurer jdbcAuthentication = new CustomJdbcUserDetailsManagerConfigurer(customJdbcUserDetailsManager());
            jdbcAuthentication.dataSource(dataSource)
                    .withUser("dave").password("secret").roles("USER")
                    .and()
                    .withUser("anil").password("password").roles("ADMIN");
            jdbcAuthentication.initUser();*/

            auth
                    .userDetailsService(customJdbcUserDetailsManager());
        }


    }
}

