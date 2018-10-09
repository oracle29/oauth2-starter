package com.security.exception;

import com.alibaba.fastjson.JSON;
import com.security.vo.OauthBaseVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by oracle on 2017-09-07.
 */
public class Oauth2ExceptionHandler {
    public void translateException(OauthBaseVo oauthBaseErrorVo, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.println(JSON.toJSONString(oauthBaseErrorVo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
