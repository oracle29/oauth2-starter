package com.security.validator;


import com.security.vo.OauthBaseVo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈圣融 on 2017-07-29.
 */
public class Oauth2ParamsValidator {
    public static String GRANT_TYPE = "grant_type";
    public static String CLIENT_ID = "client_id";
    public static String CLIENT_SECRET = "client_secret";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String TIMESTAMP="timestamp";
    public static String SIGN="sign";
    private static String[] validateStrs = new String[]{GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD,TIMESTAMP,SIGN};

    public static String ERROR_CODE_GRANT_TYPE_ABSENT = "1000";
    public static String ERROR_CODE_CLIENT_ID_ABSENT = "1001";
    public static String ERROR_CODE_CLIENT_SECRET_ABSENT = "1002";
    public static String ERROR_CODE_USERNAME_ABSENT = "1003";
    public static String ERROR_CODE_PASSWORD_ABSENT = "1004";
    public static String ERROR_CODE_TOKEN_ABSENT = "1005";
    public static String ERROR_CODE_TIMESTAMP_ABSENT="1006";
    public static String ERROR_CODE_SIGN_ABSENT="1007";

    private static String[] validateStrsAbsent = new String[]{ERROR_CODE_GRANT_TYPE_ABSENT, ERROR_CODE_CLIENT_ID_ABSENT, ERROR_CODE_CLIENT_SECRET_ABSENT, ERROR_CODE_USERNAME_ABSENT, ERROR_CODE_PASSWORD_ABSENT,ERROR_CODE_TIMESTAMP_ABSENT,ERROR_CODE_SIGN_ABSENT};


    public static String ERROR_CODE_GRANT_TYPE_ERROR = "11005";
    public static String ERROR_CODE_CLIENT_ID_ERROR = "11006";
    public static String ERROR_CODE_CLIENT_SECRET_ERROR = "11007";
    public static String ERROR_CODE_USERNAME_ERROR = "11008";
    public static String ERROR_CODE_PASSWORD_ERROR = "11009";

    public static String ERROR_CODE_AUTHENTICATION_FAIL = "11010";
    public static String ERROR_CODE_AUTHENTICATION_ACCESSDENIED = "11011";
    public static String ERROR_CODE_TOKEN_ERROR = "11012";
    public static String ERROR_CODE_SYSTEM_ERROR = "11013";
    public static String ERROR_CODE_TOKEN_EXPIRY= "11014";
    public static String ERROR_CODE_REFRESH_TOKEN_ERROR= "11015";
    public static String ERROR_CODE_TIMESTAMP_ERROR="11016";
    public static String ERROR_CODE_SIGN_ERROR="11017";

    public static Map<String, String> oauth2ErrorMap = new HashMap<>();

    static {
        oauth2ErrorMap.put(ERROR_CODE_GRANT_TYPE_ABSENT, GRANT_TYPE + "缺失");
        oauth2ErrorMap.put(ERROR_CODE_CLIENT_ID_ABSENT, CLIENT_ID + "缺失");
        oauth2ErrorMap.put(ERROR_CODE_CLIENT_SECRET_ABSENT, CLIENT_SECRET + "缺失");
        oauth2ErrorMap.put(ERROR_CODE_USERNAME_ABSENT, USERNAME + "缺失");
        oauth2ErrorMap.put(ERROR_CODE_PASSWORD_ABSENT, PASSWORD + "缺失");
        oauth2ErrorMap.put(ERROR_CODE_TOKEN_ABSENT,  "access_token缺失");
        oauth2ErrorMap.put(ERROR_CODE_TIMESTAMP_ABSENT,TIMESTAMP+"缺失");
        oauth2ErrorMap.put(ERROR_CODE_SIGN_ABSENT,SIGN+"缺失");

        oauth2ErrorMap.put(ERROR_CODE_GRANT_TYPE_ERROR, GRANT_TYPE + "不支持");
        oauth2ErrorMap.put(ERROR_CODE_CLIENT_ID_ERROR, CLIENT_ID + "不存在");
        oauth2ErrorMap.put(ERROR_CODE_CLIENT_SECRET_ERROR, CLIENT_SECRET + "错误");
        oauth2ErrorMap.put(ERROR_CODE_USERNAME_ERROR, USERNAME + "不存在");
        oauth2ErrorMap.put(ERROR_CODE_PASSWORD_ERROR, PASSWORD + "错误");
        oauth2ErrorMap.put(ERROR_CODE_TIMESTAMP_ERROR,TIMESTAMP+"错误");
        oauth2ErrorMap.put(ERROR_CODE_SIGN_ERROR,SIGN+"错误");
        oauth2ErrorMap.put(ERROR_CODE_TOKEN_ERROR,  "access_token错误");
        oauth2ErrorMap.put(ERROR_CODE_SYSTEM_ERROR,  "系统异常");
        oauth2ErrorMap.put(ERROR_CODE_TOKEN_EXPIRY,  "access_token过期");

        oauth2ErrorMap.put(ERROR_CODE_AUTHENTICATION_FAIL, "认证失败,认证信息有误");
        oauth2ErrorMap.put(ERROR_CODE_AUTHENTICATION_ACCESSDENIED, "无访问权限");
        oauth2ErrorMap.put(ERROR_CODE_REFRESH_TOKEN_ERROR, "refresh_token错误");
    }

    public static OauthBaseVo validate(Map requestMap) {
        OauthBaseVo oauthBaseVo = null;
        for (int t = 0; t < validateStrs.length; t++) {  // /oauth/token参数是否为空校验
            if (StringUtils.isEmpty(requestMap.get(validateStrs[t]))) {
                oauthBaseVo = new OauthBaseVo(validateStrsAbsent[t], oauth2ErrorMap.get(validateStrsAbsent[t]));
                break;  //每次校验一个缺失参数，返回该缺失参数错误码
            }
        }
        return oauthBaseVo;
    }
}
