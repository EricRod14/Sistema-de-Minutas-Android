package com.example.minutas;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ModuloEmail {
 // Se encarga del envio del correo, usando los datos enviados por EnviarCorreo
 // Como nota, si se usa gmail se debe conseguir la contraseña de aplicacion en la seccion
    // Seguridad en "Tu cuenta google" ahi la encontre.
    public static void sendEmail(final String to, final String subject, final String body, final String date, final String place, final String time) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                sendEmailInBackground(to, subject, body, date, place, time);
                return null;
            }
        }.execute();
    }

    private static void sendEmailInBackground(String to, String subject, String body, String date, String place, String time) {
        final String username = "Poner correo";
        final String password = "Poner contraseña";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            String mensaje = "Fecha de la reunión: " + date + "\nHora de la reunión: " + time + "\nLugar de la reunión: " + place + "\n\n" + body;

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(mensaje);

            Transport.send(message);

            Log.i("ModuloEmail", "Correo enviado con éxito.");

        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("EnviarCorreo", "Error al enviar el correo: " + e.getMessage());
        }
    }
}
