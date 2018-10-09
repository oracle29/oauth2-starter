package com.security.oauthfilter;

import com.security.exception.Oauth2ExceptionHandler;
import com.security.validator.Oauth2ParamsValidator;
import com.security.vo.OauthBaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 陈圣融 on 2017-07-29.
 */
public class Oauth2InfoExceptionFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(Oauth2InfoExceptionFilter.class);
    private Oauth2ExceptionHandler oauth2ExceptionHandler = new Oauth2ExceptionHandler();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        OauthBaseVo oauthBaseErrorVo = null;
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception exception) {
            if (exception instanceof NestedServletException && exception instanceof Exception) {
                exception = (Exception) exception.getCause();
            }
            oauthBaseErrorVo = new OauthBaseVo();
            if (exception instanceof UsernameNotFoundException) {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_USERNAME_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_USERNAME_ERROR));
            } else if (exception instanceof InvalidGrantException) {  // password错误
                if (exception.getMessage().startsWith("Invalid refresh token:")) {
                    oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_PASSWORD_ERROR);
                    oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_REFRESH_TOKEN_ERROR));
                } else {
                    oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_PASSWORD_ERROR);
                    oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_PASSWORD_ERROR));
                }
            } else if (exception instanceof InvalidClientException) {    //client_id错误
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_CLIENT_ID_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_CLIENT_ID_ERROR));
            } else if (exception instanceof UnsupportedGrantTypeException) {   //grant_type 错误
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_GRANT_TYPE_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_GRANT_TYPE_ERROR));
            } else {
                logger.error("系统异常", exception);
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_SYSTEM_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_SYSTEM_ERROR));
            }
            logger.error("there is occured a error", exception);
            oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
        }
    }

    @Override
    public void destroy() {

    }

    public Oauth2ExceptionHandler getOauth2ExceptionHandler() {
        return oauth2ExceptionHandler;
    }

    public void setOauth2ExceptionHandler(Oauth2ExceptionHandler oauth2ExceptionHandler) {
        this.oauth2ExceptionHandler = oauth2ExceptionHandler;
    }
}
