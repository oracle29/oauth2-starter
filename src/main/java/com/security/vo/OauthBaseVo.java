package com.security.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈圣融 on 2017-07-28.
 */
public class OauthBaseVo {

    public static String SECURITY_OUATH2_CODE_UNAUTHENTICATED = "401";   //认证失败代码
    public static Map<String, Object> oauthBaseMessage = new HashMap<>();

    static {
        oauthBaseMessage.put(SECURITY_OUATH2_CODE_UNAUTHENTICATED, "认证失败");
    }

    private String resultCode;
    private String resultMessage;

    public OauthBaseVo() {
    }

    public OauthBaseVo(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
