package org.techstage.backendapplication.model.token.email;

public interface EmailSender {

    void send(String to, String email);
}
