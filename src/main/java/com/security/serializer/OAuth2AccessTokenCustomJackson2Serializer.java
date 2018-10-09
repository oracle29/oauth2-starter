package com.security.serializer;/*
 * Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.security.token.Oauth2CustomAccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Provides the ability to serialize an {@link OAuth2AccessToken} with jackson2 by implementing {@link com.fasterxml.jackson.databind.JsonDeserializer}.
 * Refer to {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson1Deserializer} to learn more about the JSON format that is used.
 *
 * @author Rob Winch
 * @author Brian Clozel
 * @see org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer
 */
public final class OAuth2AccessTokenCustomJackson2Serializer extends StdSerializer<OAuth2AccessToken> {

    public OAuth2AccessTokenCustomJackson2Serializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public void serialize(OAuth2AccessToken token, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonGenerationException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jgen.writeStartObject();
        jgen.writeStringField(Oauth2CustomAccessToken.UID, UUID.randomUUID().toString());
        jgen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
        OAuth2RefreshToken refreshToken = token.getRefreshToken();
        if (refreshToken != null) {
            jgen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
        }
        jgen.writeStringField(Oauth2CustomAccessToken.TIME, dateFormat.format(new Date()));

//        jgen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
        Date expiration = token.getExpiration();
        if (expiration != null) {
            long now = System.currentTimeMillis();
            jgen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
        }
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            Date refreshTokenexpiration = ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration();
            if (refreshTokenexpiration != null) {
                long now = System.currentTimeMillis();
                jgen.writeNumberField(Oauth2CustomAccessToken.REFRESH_TOKEN_EXPIRES,(refreshTokenexpiration.getTime() - now) / 1000);
            }
        }

//        Set<String> scope = token.getScope();
//        if (scope != null && !scope.isEmpty()) {
//            StringBuffer scopes = new StringBuffer();
//            for (String s : scope) {
//                Assert.hasLength(s, "Scopes cannot be null or empty. Got " + scope + "");
//                scopes.append(s);
//                scopes.append(" ");
//            }
//            jgen.writeStringField(OAuth2AccessToken.SCOPE, scopes.substring(0, scopes.length() - 1));
//        }
//        Map<String, Object> additionalInformation = token.getAdditionalInformation();
//        for (String key : additionalInformation.keySet()) {
//            jgen.writeObjectField(key, additionalInformation.get(key));
//        }
        jgen.writeEndObject();
    }
}