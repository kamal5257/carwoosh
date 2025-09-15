package com.carwoosh.utils;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    // Email validation regex
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Password: Min 8 characters, at least 1 uppercase, 1 lowercase, 1 number
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    // Username: 4–20 characters, no spaces
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{4,20}$";
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);

    // OTP: 6 digits
    private static final String OTP_REGEX = "^\\d{6}$";
    private static final Pattern OTP_PATTERN = Pattern.compile(OTP_REGEX);

    // Mobile number: 10 digits (India format)
    private static final String MOBILE_REGEX = "^[6-9]\\d{9}$";
    private static final Pattern MOBILE_PATTERN = Pattern.compile(MOBILE_REGEX);

    // Org name: only letters and spaces, min 3 chars
    private static final String ORG_REGEX = "^[a-zA-Z ]{3,50}$";
    private static final Pattern ORG_PATTERN = Pattern.compile(ORG_REGEX);

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidOtp(String otp) {
        return otp != null && OTP_PATTERN.matcher(otp).matches();
    }

    public static boolean isValidMobile(String mobileNo) {
        return mobileNo != null && MOBILE_PATTERN.matcher(mobileNo).matches();
    }

    public static boolean isValidOrgName(String orgName) {
        return orgName != null && ORG_PATTERN.matcher(orgName).matches();
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
