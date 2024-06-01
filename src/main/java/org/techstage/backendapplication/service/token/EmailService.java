package org.techstage.backendapplication.service.token;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.token.email.EmailSender;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    @Async
    @SneakyThrows
    public void send(String to, String email, String subject) {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(email, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("techstageit@gmail.com");
        mailSender.send(mimeMessage);
    }

    public static String buildEmailResetPsw(String name, String link) {
        return "PAG RESET";
    }

    public static String buildEmailVerification(String name, String link) {
        return "<div style=\"align-self:center; width: 80%;background-color: #000; color: #fff; font-family: Arial, sans-serif; margin: 0; padding: 0; background-image: url('https://images.unsplash.com/photo-1574282801963-57260dd53bf7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fG5pZ2h0JTIwY2x1YnxlbnwwfHwwfHx8MA%3D%3D'); background-size: cover; background-repeat: no-repeat; background-position: center;\">\n" +
                "        <div class=\"container\" style=\"max-width: 600px; margin: 0 auto; padding: 20px; text-align: center; background-color: rgba(0, 0, 0, 0.8);\">\n" +
                "            <div class=\"header\" style=\"background-color: #a6003d; padding: 20px;\">\n" +
                "                <h1 style=\"margin: 0; font-size: 24px; text-transform: uppercase;\">E-mail di conferma</h1>\n" +
                "            </div>\n" +
                "            <div class=\"content\" style=\"padding: 20px; background-color: #111;\">\n" +
                "                <img src=\"http://51.254.36.232:8085/logo.png\" style=\"max-width: 100%; height: 60px;\">\n" +
                "                <p style=\"font-size: 18px; line-height: 1.5;\">Ciao " + name + ",</p>\n" +
                "                <p style=\"font-size: 18px; line-height: 1.5;\">Grazie per esserti registrato. Sei a un passo dall'entrare a far parte del Blackmoon!</p>\n" +
                "                <p style=\"font-size: 18px; line-height: 1.5;\">Clicca sul pulsante qui sotto per confermare il tuo account e iniziare subito a scoprire eventi imperdibili, divertimento e tanto altro.</p>\n" +
                "                <a href=\""+link+"\" style=\"display: inline-block; margin-top: 20px; padding: 15px 30px; font-size: 18px; color: #fff; background-color: #a6003d; text-decoration: none; border-radius: 5px; text-transform: uppercase;\">Conferma il tuo Account</a>\n" +
                "            </div>\n" +
                "            <div class=\"footer\" style=\"margin-top: 20px; font-size: 14px; color: #666;\">\n" +
                "                <p>Se non hai richiesto questa email, puoi ignorarla.</p>\n" +
                "                <p>&copy; 2024 Blackmoon Torino. Tutti i diritti riservati.</p>\n" +
                "                <p><img src=\"resources/images/techstage.webp\" style=\"max-width: 100%; height: 90px;\"></p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>";
    }
}
