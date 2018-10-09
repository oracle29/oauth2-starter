package com.security.exception;

import com.security.validator.Oauth2ParamsValidator;
import com.security.vo.OauthBaseVo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 陈圣融 on 2017-07-28.
 */
public class Oauth2ExceptionTranslator implements WebResponseExceptionTranslator, AuthenticationEntryPoint {
    private Oauth2ExceptionHandler oauth2ExceptionHandler = new Oauth2ExceptionHandler();

    /**
     * 处理 usernName password校验不通过
     *
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception { //将异常抛出，由过异常滤器统一处理
        throw e;
    }

    /**
     * 处理 client_id client_secret校验不通过
     *
     * @param servletRequest
     * @param servletResponse
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest servletRequest, HttpServletResponse servletResponse, AuthenticationException exception) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        OauthBaseVo oauthBaseErrorVo = null;
        oauthBaseErrorVo = new OauthBaseVo();
        if (exception instanceof UsernameNotFoundException) {   //client_id错误
            oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_CLIENT_ID_ERROR);
            oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_CLIENT_ID_ERROR));
        } else if (exception instanceof BadCredentialsException) {  // client_secret错误
            oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_CLIENT_SECRET_ERROR);
            oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_CLIENT_SECRET_ERROR));
        } else if (exception instanceof InsufficientAuthenticationException) {  // token错误
            if (exception.getMessage().startsWith("Access token expired:")) {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TOKEN_EXPIRY);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TOKEN_EXPIRY));
            } else {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TOKEN_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TOKEN_ERROR));
            }
        }
        oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
    }

    public Oauth2ExceptionHandler getOauth2ExceptionHandler() {
        return oauth2ExceptionHandler;
    }

    public void setOauth2ExceptionHandler(Oauth2ExceptionHandler oauth2ExceptionHandler) {
        this.oauth2ExceptionHandler = oauth2ExceptionHandler;
    }
}
