package org.techstage.backendapplication.web.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
