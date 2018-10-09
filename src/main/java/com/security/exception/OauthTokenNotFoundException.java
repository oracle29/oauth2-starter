package com.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 陈圣融 on 2017-07-30.
 */
public class OauthTokenNotFoundException extends AuthenticationException {

    public OauthTokenNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public OauthTokenNotFoundException(String msg) {
        super(msg);
    }
}
