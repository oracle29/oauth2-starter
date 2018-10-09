package com.security.oauthfilter;

import com.security.exception.Oauth2ExceptionHandler;
import com.security.exception.OauthTokenNotFoundException;
import com.security.validator.Oauth2ParamsValidator;
import com.security.vo.OauthBaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 陈圣融 on 2017-07-29.
 */
public class Oauth2AccessDeniedExceptionFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(Oauth2AccessDeniedExceptionFilter.class);

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
//            if (!oauth2ParamsHandler.isNoCheckUrl(request)) {
                String accessToken = request.getParameter("access_token");
                if (StringUtils.isEmpty(accessToken)) {
                    throw new OauthTokenNotFoundException("token not found");
                }
//            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException ex) {
            logger.error("Exception ...",ex);
            throw ex;
        } catch (Exception exception) {
            oauthBaseErrorVo = new OauthBaseVo();
            if (exception instanceof OauthTokenNotFoundException) {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TOKEN_ABSENT);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TOKEN_ABSENT));
            } else if (exception instanceof AccessDeniedException) {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_AUTHENTICATION_ACCESSDENIED);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_AUTHENTICATION_ACCESSDENIED));
            }
            logger.error("there is a excepton", exception);
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
