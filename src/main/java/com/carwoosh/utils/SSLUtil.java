package com.carwoosh.utils;


import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SSLUtil {
    public static void disableSSLCertificateChecking() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {}
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

