package com.security.oauthfilter;

import com.security.exception.Oauth2ExceptionHandler;
import com.security.validator.Oauth2ParamsValidator;
import com.security.vo.OauthBaseVo;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import utils.Lang;
import utils.security.MD5Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 陈圣融 on 2017-07-29.
 */

/**
 * /oauth/token参数缺失校验
 */
public class Oauth2ParamsValidateFilter implements Filter {
    private Oauth2ExceptionHandler oauth2ExceptionHandler = new Oauth2ExceptionHandler();

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        PathMatcher pathMatcher = new AntPathMatcher();
        if (pathMatcher.match(request.getContextPath() + "/oauth/token", request.getRequestURI()) && "password".equals(request.getParameter("grant_type"))) {
            OauthBaseVo oauthBaseErrorVo = Oauth2ParamsValidator.validate(request.getParameterMap());
            if(oauthBaseErrorVo==null){
                oauthBaseErrorVo=new OauthBaseVo();
            }
            String time=request.getParameter("timestamp");
            if(Lang.isEmpty(time)){
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ABSENT);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ABSENT));
                oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
                return;
            }
            Long requestDate=null;
            try {
                requestDate=format.parse(time).getTime();//将请求的时间字符串转换为long类型
            } catch (ParseException e) {
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ERROR));
                oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
                return;
            }
            Long nowDate=new Date().getTime();//获取当前时间
            if(Math.abs(nowDate-requestDate)>30*60*1000){
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ERROR));
                oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
                return;
            }

            String sign=request.getParameter("sign");
            if(Lang.isEmpty(sign)){
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_SIGN_ABSENT);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_SIGN_ABSENT));
                oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
                return;
            }
            String tempSign="";
            if(!Lang.isEmpty(request.getParameter("scope"))){
                tempSign=request.getParameter("client_secret")+request.getParameter("timestamp")+request.getParameter("client_id")+request.getParameter("username")+request.getParameter("password")+request.getParameter("grant_type")+request.getParameter("scope")+request.getParameter("client_secret");
            }else {
                tempSign=request.getParameter("client_secret")+request.getParameter("timestamp")+request.getParameter("client_id")+request.getParameter("username")+request.getParameter("password")+request.getParameter("grant_type")+request.getParameter("client_secret");
            }
            String encryptSign="";
            try {
                encryptSign=MD5Utils.encrypt(tempSign).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!encryptSign.equals(sign)){
                oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_SIGN_ERROR);
                oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_SIGN_ERROR));
                oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
                return;
            }
        }
        filterChain.doFilter(request, response);

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
