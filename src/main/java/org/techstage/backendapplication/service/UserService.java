package org.techstage.backendapplication.service;

import org.techstage.backendapplication.domain.User;
import org.techstage.backendapplication.web.dto.UserLoginDTO;
import org.techstage.backendapplication.web.dto.UserRegistrationDTO;

public interface UserService {
    void save(UserRegistrationDTO registrationDTO);

    void login(UserLoginDTO loginDTO);
}
