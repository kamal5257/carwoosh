package com.carwoosh.constant;

public enum APPServiceCode {
    // 0-10 && 101-110 specfic for backend creation
    APP_001("APP_001", "Request processed successfully"),
    APP_002("APP_002", "Invalid email format"),
    APP_003("APP_003", "Invalid username"),
    APP_004("APP_004", "weak password"),
    APP_005("APP_005", "Invalid mobile number"),
    APP_006("APP_006", "Invalid organisation name"),
    APP_007("APP_007", "Email already exist !"),
    APP_008("APP_008", "Username already exist !"),
    APP_009("APP_009", "Unauthorized User"),
    APP_030("APP_030", "OTP is not verified"),
    APP_040("APP_040", "Employee Id not in request"),
    APP_904("APP_904", "No records found"), APP_908("APP_908", "User does not exist"),
    APP_909("APP_909","Missing or invalid Authorization header"),
    APP_910("APP_910","Invalid or expired token"),
    APP_996("APP_996", "Invalid request"), APP_997("APP_997", "Bad Request"), APP_998("APP_998", "Invalid Credentials"),
    APP_999("APP_999", "Unable to processs, please try again later");
    String statusCode = null;
    String statusDesc = null;

    private APPServiceCode( String statusCode, String statusDesc )
    {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public String getStatusCode()
    {
        return statusCode;
    }

    public String getStatusDesc()
    {
        return statusDesc;
    }

    public static APPServiceCode getServiceByErrorKey( String errorKey )
    {
        for ( APPServiceCode serviceCode : APPServiceCode.values() )
        {
            if ( serviceCode.getStatusCode().equals( errorKey ) )
            {
                return serviceCode;
            }
        }
        return APPServiceCode.APP_997;
    }
}
