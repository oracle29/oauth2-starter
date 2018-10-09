package com.security.token;

import com.alibaba.fastjson.annotation.JSONField;
import com.security.serializer.OAuth2AccessTokenCustomJackson2Serializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Date;

/**
 * Created by oracle on 2017-08-31.
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OAuth2AccessTokenCustomJackson2Serializer.class)
public class Oauth2CustomAccessToken extends DefaultOAuth2AccessToken {
    public static final String UID = "UID";
    public static final String TIME = "time";
    public static final String REFRESH_TOKEN_EXPIRES = "refresh_token_expires";

    @JSONField(format="yyyy-MM-dd HH:mm:ss")     private Date time;

    public Oauth2CustomAccessToken(String value) {
        super(value);
    }

    public Oauth2CustomAccessToken(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
