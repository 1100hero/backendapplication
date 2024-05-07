package org.techstage.backendapplication.service;

import org.techstage.backendapplication.domain.User;
import org.techstage.backendapplication.web.dto.UserRegistrationDTO;

public interface UserService {
    User save(UserRegistrationDTO registrationDTO);
}
