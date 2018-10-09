package com.security.oauthfilter;

import com.security.exception.Oauth2ExceptionHandler;
import com.security.validator.Oauth2ParamsValidator;
import com.security.vo.OauthBaseVo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bande on 2017/9/8.
 */
public class Oauth2TpScoreApiFilter implements Filter {

    private Oauth2ExceptionHandler oauth2ExceptionHandler = new Oauth2ExceptionHandler();
    static ThreadLocal<DateFormat> formatThreadLocal ;
    static {
        formatThreadLocal =new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        };
    }
//    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        OauthBaseVo oauthBaseErrorVo=new OauthBaseVo();
        String time=request.getParameter("timestamp");
        if(time==null){
            oauthBaseErrorVo.setResultCode(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ABSENT);
            oauthBaseErrorVo.setResultMessage(Oauth2ParamsValidator.oauth2ErrorMap.get(Oauth2ParamsValidator.ERROR_CODE_TIMESTAMP_ABSENT));
            oauth2ExceptionHandler.translateException(oauthBaseErrorVo, response);
            return;
        }
        Long requestDate=null;
        try {
            DateFormat format = formatThreadLocal.get();
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
