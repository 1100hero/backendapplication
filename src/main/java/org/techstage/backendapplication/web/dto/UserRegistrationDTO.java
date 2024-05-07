package org.techstage.backendapplication.web.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {

    private String name;
    private String surname;
    private String telephone;
    private String email;
    private String password;
}
