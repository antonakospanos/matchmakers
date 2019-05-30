package com.workable.matchmakers.service;

import javassist.NotFoundException;
import com.workable.matchmakers.dao.converter.UserConverter;
import com.workable.matchmakers.dao.model.User;
import com.workable.matchmakers.dao.repository.MovieRepository;
import com.workable.matchmakers.dao.repository.UserRepository;
import com.workable.matchmakers.web.dto.patch.PatchDto;
import com.workable.matchmakers.web.dto.response.CreateResponseData;
import com.workable.matchmakers.web.dto.users.UserBaseDto;
import com.workable.matchmakers.web.dto.users.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserConverter userConverter;

    @Autowired
    HashService hashService;


    @Transactional
    public CreateResponseData create(UserBaseDto userBaseDto) {
        User user;
        UserDto userDto = new UserDto(userBaseDto);
        User userWithUsername = userRepository.findByUsername(userDto.getUsername());
        User userWithEmail = userRepository.findByEmail(userDto.getEmail());

        if (userWithUsername != null) {
            throw new IllegalArgumentException("User with username '" + userDto.getUsername() + "' already exists!");
        } else if (userWithEmail != null) {
            throw new IllegalArgumentException("User with email '" + userDto.getEmail() + "' already exists!");
        } else {
            // Add new User in DB
            user = userDto.toEntity();
            userConverter.setPassword(userDto, user);

            userRepository.save(user);

            logger.info("New User added: " + user);
        }

        return new CreateResponseData(user.getExternalId().toString());
    }

    @Transactional
    public CreateResponseData update(String username, List<PatchDto> patches) {
        validateUser(username);
        User user = userRepository.findByUsername(username);

        return update(user, patches);
    }

    @Transactional
    public CreateResponseData update(UUID externalId, List<PatchDto> patches) {
        validateUser(externalId);
        User user = userRepository.findByExternalId(externalId);

        return update(user, patches);
    }

    @Transactional
    public CreateResponseData update(User user, List<PatchDto> patches) {
        patches.stream().forEach(patchDto -> {
            // Validate User patches
            if (patchDto.getField().equals("username")) {
                validateNewUsername(user.getUsername(), patchDto.getValue());
            }
            userConverter.updateUser(patchDto, user);
        });

        userRepository.save(user);

        logger.info("User updated: " + user);

        return new CreateResponseData(user.getExternalId().toString());
    }

    @Transactional
    public User find(UUID externalId) {
        return userRepository.findByExternalId(externalId);
    }

    @Transactional
    public UserDto find(String username, String password) throws NotFoundException {
        List<UserDto> users = list(username);

        if (users == null || users.isEmpty()) {
            throw new NotFoundException("No user found with username '" + username + "'");
        } else if (users.size() > 1) {
            throw new RuntimeException("Multiple users found with username '" + username + "'");
        } else {
            UserDto userDto = users.get(0);
            if (!hashService.matches(password, userDto.getPassword())) {
                throw new AuthorizationServiceException("Invalid password '" + password + "'");
            } else {
                return userDto;
            }
        }
    }

    @Transactional
    public List<UserDto> list(UUID externalId) {
        List<UserDto> userDtos;

        if (externalId != null) {
            User user = userRepository.findByExternalId(externalId);
            userDtos = list(user);
        } else {
            userDtos = listAll();
        }

        return userDtos;
    }

    @Transactional
    public List<UserDto> list(String username) {
        List<UserDto> userDtos;

        if (StringUtils.isNotBlank(username)) {
            User user = userRepository.findByUsername(username);
            userDtos = list(user);
        } else {
            userDtos = listAll();
        }

        return userDtos;
    }

    @Transactional
    public List<UserDto> list(User user) {
        List<UserDto> userDtos = new ArrayList<>();

        if (user != null) {
            UserDto userDto = new UserDto().fromEntity(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Transactional
    public List<UserDto> listAll() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto().fromEntity(user))
                .collect(Collectors.toList());

        return userDtos;
    }

    // ************************ Validations ************************ //
    @Transactional
    public void validateUser(String username) {
        if (StringUtils.isNotBlank(username)) {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User with username '" + username + "' does not exist!");
            }
        }
    }

    @Transactional
    public void validateUser(UUID externalId) {
        if (externalId != null) {
            User user = userRepository.findByExternalId(externalId);
            if (user == null) {
                throw new IllegalArgumentException("User with id '" + externalId + "' does not exist!");
            }
        }
    }

    @Transactional
    public void validateNewUsername(String oldUsername, String newUsername) {
        if (StringUtils.isNotBlank(newUsername)) {
            // Check that resource does not conflict
            User user = userRepository.findByUsername(newUsername);
            if (!oldUsername.equals(newUsername) && user != null) {
                // Illegal username replacement
                throw new IllegalArgumentException("User with username '" + newUsername + "' already exists!");
            }
        }
    }
}
