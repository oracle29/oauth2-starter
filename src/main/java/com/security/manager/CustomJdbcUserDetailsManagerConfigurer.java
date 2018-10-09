package com.security.manager;

import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

/**
 * Created by oracle on 2017-09-07.
 */
public class CustomJdbcUserDetailsManagerConfigurer extends JdbcUserDetailsManagerConfigurer {
    public CustomJdbcUserDetailsManagerConfigurer(JdbcUserDetailsManager manager) {
        super(manager);
    }

    public CustomJdbcUserDetailsManagerConfigurer() {
    }

    public void initUser() {
        try {
            initUserDetailsService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
