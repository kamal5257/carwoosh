package com.carwoosh.constant;

import java.time.format.DateTimeFormatter;

public final class ConstantStrings
{
    public final static String            No                 = "N";
    public final static String            Yes                = "Y";
    public final static String            SPACE              = " ";
    public static final String            SomethingWentWrong = "Something went wrong.";
    public static final String            METHOD_NAME_SMS    = "notificationAppSendSms";
    public static final String            METHOD_NAME_MAIL   = "notificationAppSendMail";
    public static final String            LANGUAGE_KEY       = "Accept-Language";
    public static final String            SERVICE_KEY        = "serviceParam";
    public static final String            REQUEST_KEY        = "requestParam";
    public static final String            DEFAULT_LANGUAGE   = "en";
    public static final String            ANDROID            = "android";
    public static final String            IOS                = "ios";
    public static final String            DEVROLE            = "D";
    public static final String            OWNERROLE          = "O";
    public static final String            ASCENDING          = "ASC";
    public static final String            DESCENDING         = "DESC";
    public static final DateTimeFormatter DTF                = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm:ss" );
    public static final String            ENDPOINT           = "E";
    public static final String            BACKEND            = "B";
    public static final String            PROJECT_DOMAIN     = "P";
    public static final String            ADMINROLE          = "SA";
}
