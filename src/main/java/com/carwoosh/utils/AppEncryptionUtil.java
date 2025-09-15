package com.carwoosh.utils;

import java.time.LocalDateTime;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carwoosh.constant.ConstantStrings;



public class AppEncryptionUtil
{
    private static final String keyMain    = "37ZA3D89B64C115122DF9178C8R99c1x";
    private static final String initVector = "213A26DBB4A358C5";
    final static Logger         logger     = LogManager.getLogger( AppEncryptionUtil.class );

    public static String encrypt( String value )
    {
        try
        {
            Base64 b64 = new Base64();
            IvParameterSpec iv = new IvParameterSpec( initVector.getBytes( "UTF-8" ) );
            SecretKeySpec skeySpec = new SecretKeySpec( keyMain.getBytes( "UTF-8" ), "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
            cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv );
            byte[] encrypted = cipher.doFinal( value.getBytes() );
            return b64.encodeToString( encrypted );
        }
        catch ( Exception ex )
        {
            //change_vishal
            logger.error( "Notification :: " + "AppEncryptionUtil_encrypt :: " + " Exception :: "
                    + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: " + ex.toString() );
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt( String encrypted )
    {
        try
        {
            Base64 b64 = new Base64();
            IvParameterSpec iv = new IvParameterSpec( initVector.getBytes( "UTF-8" ) );
            SecretKeySpec skeySpec = new SecretKeySpec( keyMain.getBytes( "UTF-8" ), "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
            cipher.init( Cipher.DECRYPT_MODE, skeySpec, iv );
            byte[] original = cipher.doFinal( b64.decode( encrypted ) );
            return new String( original );
        }
        catch ( Exception ex )
        {
            //change_vishal
            logger.error( "Notification :: " + "AppEncryptionUtil_encrypt :: " + " Exception :: "
                    + ConstantStrings.DTF.format( LocalDateTime.now() ) + " :: " + ex.toString() );
            ex.printStackTrace();
        }
        return null;
    }

    public static void main( String[] args )
    {
        System.out.println( encrypt( "Kamal@123" ) );
//        decrypt( "sip7sMU83eY+BVTU09VcTnYez2DstaLUZJFRuurEScc=" );
    }
}
