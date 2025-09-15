package com.carwoosh.utils;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carwoosh.model.EmailDetails;

@Service
public class EmailUtil {

    private static final Logger logger = LogManager.getLogger(EmailUtil.class);

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public boolean sendSimpleMail(EmailDetails details) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Required for port 587
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        System.setProperty("https.protocols", "TLSv1.2");


        // Optional timeouts
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true); // Optional: logs SMTP communication

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse("kamal.chadha651@gmail.com"));
            message.setSubject(details.getSubject());
            message.setText(details.getMsgBody());

            Transport.send(message);
            System.out.println("✅ Email sent successfully!");
            return true;
        } catch (Exception e) {
            logger.error("❌ Failed to send email: ", e);
            return false;
        }
    }
}
