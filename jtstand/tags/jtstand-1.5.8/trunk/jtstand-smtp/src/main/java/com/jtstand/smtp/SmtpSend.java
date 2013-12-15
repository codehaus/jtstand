package com.jtstand.smtp;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Hello world!
 *
 */
public class SmtpSend {

    private static final String SMTP_PORT = "465";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public static void send() {

        ///////// set this variable to be your SMTP host

        String mailHost = "smtp.gmail.com";
        String to = "username@gmail.com";
        String from = "username@gmail.com";
        String subject = "this is a test";
        String body = "message body";

        if ((from != null) && (to != null) && (subject != null) && (body != null)) // we have mail to send
        {
            try {
                //Get system properties
                Properties props = System.getProperties();

                //Specify the desired SMTP server
                props.put("mail.smtp.host", mailHost);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", SMTP_PORT);
                props.put("mail.smtp.socketFactory.port", SMTP_PORT);
                props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
                props.put("mail.smtp.socketFactory.fallback", "false");

                // create a new Session object
                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {

                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        "albert.kurucz@sanmina-sci.com", "mypassword");
                            }
                        });


                // create a new MimeMessage object (using the Session created above)
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});
                message.setSubject(subject);
                message.setContent(body, "text/plain");

                Transport.send(message);


                // it worked!
                System.out.println("Message to " + to + " was successfully sent.");

            } catch (Throwable t) {
                System.out.println("Unable to send message");
                t.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //TBD
        System.out.println("Hello World!");
        SmtpSend.send();
    }
}
