package com.example.julio.sistemareserva;

import android.app.Activity;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class verificaEmail {
    Session session = null;
    Context context = null;
    String correo="jcfabri93@hotmail.com";

    public void controlMail () {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tuliofabri10@gmail.com", "elpardebilY");
            }
        });

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();

    }

    public void setContext(Context c) {
        context=c;
    }
    public void setCorreo(String correo) {
        this.correo=correo;
    }


    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
                message.setSubject("Confirmacion de la cuenta");
                message.setContent("Su cuenta ha sido registrada satisfactoriamente. App Developers, TULIOOO ", "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, "Se confirmo correo", Toast.LENGTH_LONG).show();
        }
    }
}
