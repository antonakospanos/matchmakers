package com.workable.matchmakers.dao.converter;

import com.workable.matchmakers.dao.model.User;
import com.workable.matchmakers.dao.repository.UserRepository;
import com.workable.matchmakers.service.HashService;
import com.workable.matchmakers.web.dto.patch.PatchDto;
import com.workable.matchmakers.web.dto.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashService hashService;

    /**
     * Password shall be set either upon registration or upon password reset using PATCH API
     *
     * @param userDto
     * @param user
     */
    public void setPassword(UserDto userDto, User user) {
        // Store hashed password
        String hashedPassword = hashService.bCryptPassword(userDto.getPassword());
        user.setPassword(hashedPassword);
    }

    public void updateUser(PatchDto patchDto, User user) {
        String field = patchDto.getField();
        String value = patchDto.getValue();

        if ("username".equals(field)) {
            user.setUsername(value);
        } else if ("password".equals(field)) {
            String hashedPassword = hashService.bCryptPassword(value);
            user.setPassword(hashedPassword);
            user.setExternalId(UUID.randomUUID()); // Update externalId used in Authentication Bearer too!
        } else if ("name".equals(field)) {
            user.setName(value);
        } else if ("email".equals(field)) {
            user.setEmail(value);
        } else {
            throw new UnsupportedOperationException("Update of field '"+value+"' is not permitted!");
        }
    }


}
