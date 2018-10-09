package com.security.controller;

import com.security.vo.OauthBaseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 陈圣融 on 2017-07-28.
 */
@Controller
public class OauthController{

    @RequestMapping("/unoauthenticated")
    @ResponseBody
    public OauthBaseVo unauthenticated(HttpServletRequest request) {
        OauthBaseVo oauthBaseVo = (OauthBaseVo) request.getAttribute("oauthBaseErrorVo");
        return oauthBaseVo;
    }



}
