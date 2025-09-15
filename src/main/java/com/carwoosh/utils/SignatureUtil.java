package com.carwoosh.utils;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carwoosh.constant.ConstantStrings;

public class SignatureUtil
{
    final static Logger   LOGGER                  = LogManager.getLogger( SignatureUtil.class );
    private static String preShareKeyForSignature = "QD32VdbRuMa0iI0q9q7cH6FIHGcNWGdEZOLyK669";
    private static String signatureAlgorithm      = "HmacSHA256";
    public static boolean isSignatureValid( final String inSignature,
                                            final String inMethodName,
                                            final String inServiceParam,
                                            final String inClientParam,
                                            final String inTimeStamp,
                                            final String inObjectDto )
    {
        return isSignatureValid( inSignature, inMethodName, inServiceParam, inClientParam, inTimeStamp, inObjectDto,
                                 null );
    }

    public static String getSignature( String inMethodName,
                                       String inServiceParam,
                                       String inClientParam,
                                       String inTimeStamp,
                                       String inObjectDto )
    {
        return getSignature( inMethodName, inServiceParam, inClientParam, inTimeStamp, inObjectDto, null );
    }

    public static boolean isSignatureValid( final String signature,
                                            final String url,
                                            final String timestamp,
                                            final Object inObj )
    {
        boolean status = false;
        //        if ( LOGGER.isDebugEnabled() )
        //        {
        //change_vishal
        LOGGER.error( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: " + "signature - "
                + signature );
        //change_vishal
        LOGGER.error( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: url - " + url );
        //change_vishal
        LOGGER.error( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: inObj - " + inObj );
        //change_vishal
        LOGGER.error( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: timestamp - "
                + timestamp );
        //        }
        final String derivedSignature = getSignature( url, timestamp, inObj );
        //change_vishal
        LOGGER.info( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: Received Signature:"
                + signature );
        //change_vishal
        LOGGER.info( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: Derived Signature:"
                + derivedSignature );
        if ( StringUtils.isNotBlank( signature ) && StringUtils.isNotBlank( derivedSignature )
                && signature.equals( derivedSignature ) )
        {
            if ( LOGGER.isDebugEnabled() )
            {
                //change_vishal
                LOGGER.debug( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: "
                        + String.format( "Signature Valid for url %s", url ) );
            }
            status = true;
        }
        if ( LOGGER.isDebugEnabled() )
        {
            //change_vishal
            LOGGER.debug( "Notification :: " + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: "
                    + String.format( "Signature InValid for url %s", url ) );
        }
        return status;
    }

    /**
     * 
     * @param url       : /master/{serviceParam}/{clientParam}
     * @param timestamp
     * @param inObj
     * @return
     */
    public static String getSignature( String url, String timestamp, Object inObj )
    {
        StringBuffer data = new StringBuffer();
        String detail = SafeFieldUtils.readField( inObj, "param", true, String.class ) + timestamp;
        Integer recordID = SafeFieldUtils.readField( inObj, "recordId", true, Integer.class );
        String clientIPAddress = SafeFieldUtils.readField( inObj, "clientIPAddress", true, String.class );
        detail = detail + detail.hashCode();
        if ( null != recordID && recordID > 0 )
        {
            detail = detail + recordID;
        }
        if ( null != clientIPAddress && !"".equals( clientIPAddress ) )
        {
            detail = detail + clientIPAddress;
        }
        if ( StringUtils.isNotBlank( url ) )
        {
            if ( StringUtils.startsWith( url, "/" ) )
            {
                url = StringUtils.removeStart( url, "/" );
                String strArr[] = StringUtils.split( url, "/" );
                data.append( strArr[0] ).append( timestamp ).append( strArr[1] ).append( strArr[2] ).append( detail );
            }
        }
        String signature = calculateHMAC( data.toString() );
        return signature;
    }

    private static String calculateHMAC( String data )
    {
        try
        {
            SecretKeySpec signingKey = new SecretKeySpec( preShareKeyForSignature.getBytes(), signatureAlgorithm );
            Mac mac = Mac.getInstance( signatureAlgorithm );
            mac.init( signingKey );
            byte[] rawHmac = mac.doFinal( data.getBytes() );
            String derivedSignature = new String( Base64.encodeBase64( rawHmac ) );
            return derivedSignature;
        }
        catch ( GeneralSecurityException e )
        {
            //change_vishal
            LOGGER.error( "Notification :: " + " SignatureUtil_calculateHMAC :: " + "Exception :: "
                    + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: " + e.toString() );
            throw new IllegalArgumentException( e );
        }
    }

    /*    public static void main( String[] args )
    {
        String url = "/users/{serviceParam}/{userParam}";
        long timestamp = System.currentTimeMillis();
        System.out.println( "Timestamp : " + timestamp );
        String signature = SignatureUtil.getSignature( url, String.valueOf( timestamp ), "" );
        System.out.println( signature );
    }*/
    public static boolean isSignatureValid( String inSignature,
                                            String inMethodName,
                                            String inServiceParam,
                                            String inClientParam,
                                            String inTimeStamp,
                                            String inObjectDto,
                                            String inStrData )
    {
        boolean status = false;
        LOGGER.info( "Received signature - " + inSignature );
        LOGGER.info( "methodName - " + inMethodName );
        LOGGER.info( "inServiceParam - " + inServiceParam );
        LOGGER.info( "inClientParam - " + inClientParam );
        LOGGER.info( "timestamp - " + inTimeStamp );
        LOGGER.info( "inDetail - " + inObjectDto );
        final String derivedSignature = getSignature( inMethodName, inServiceParam, inClientParam, inTimeStamp,
                                                      inObjectDto, inStrData );
        LOGGER.info( "Derived Signature:" + derivedSignature );
        if ( StringUtils.isNotBlank( inSignature ) && StringUtils.isNotBlank( derivedSignature )
                && inSignature.equalsIgnoreCase( derivedSignature ) )
        {
            if ( LOGGER.isDebugEnabled() )
            {
                LOGGER.info( "Signature Valid for methodName " + inMethodName );
            }
            status = true;
        }
        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.info( "Signature InValid for methodName %s" + inMethodName );
        }
        return status;
    }

    public static String getSignature( String inMethodName,
                                       String inServiceParam,
                                       String inClientParam,
                                       String inTimeStamp,
                                       Object inObjectDto,
                                       String inStrData )
    {
        StringBuffer data = new StringBuffer();
        String detail = SafeFieldUtils.readField( inObjectDto, "param", true, String.class ) + inTimeStamp;
        Integer recordID = SafeFieldUtils.readField( inObjectDto, "recordId", true, Integer.class );
        String clientIPAddress = SafeFieldUtils.readField( inObjectDto, "clientIPAddress", true, String.class );
        detail = detail + detail.hashCode();
        if ( null != recordID && recordID > 0 )
        {
            detail = detail + recordID;
        }
        if ( null != clientIPAddress && !"".equals( clientIPAddress ) )
        {
            detail = detail + clientIPAddress;
        }
        if ( null != inStrData && !"".equals( inStrData ) )
        {
            detail = detail + inStrData;
        }
        if ( StringUtils.isValidObj( inObjectDto ) )
        {
            detail = detail + inObjectDto;
        }
        data.append( inMethodName ).append( inTimeStamp ).append( inServiceParam ).append( inClientParam )
                .append( detail );
        LOGGER.info( "Signature data" + data.toString() );
        String signature = calculateHMAC( data.toString() );
        //   String signature= data.toString();
        return signature;
    }
}
